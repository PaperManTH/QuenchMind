"""
@Author thpaperman
@Description 智能体 API — 统一入口 + 各意图端点
@Date 2026/7/7
@Version 3.0
"""
import json
import re
from fastapi import APIRouter
from fastapi.responses import StreamingResponse

from agent_service.models.request import ChatRequest, SummarizeRequest, SearchRequest, StudyPlanRequest
from agent_service.core.agent import (
    get_chat_chain, get_summarize_chain, get_study_plan_chain,
    get_rag_chain, get_exam_chain,
)
from agent_service.core.exceptions import LLMException
from agent_service.tools.web_search import semantic_search
from agent_service.api.middleware.intent import classify_intent, INTENT_DISPATCH_TABLE
from agent_service.utils.logger import logger

router = APIRouter(prefix="/api/v1")


# ── 统一智能体入口（流式） ──────────────────────────────────────

@router.post("/chat")
async def unified_chat(req: ChatRequest):
    """统一入口：意图识别 → 路由分发 → 流式响应"""
    intent = classify_intent(req.message)
    logger.info(f"意图: {intent} | session: {req.session_id} | msg: {req.message[:50]}")

    # 计划/摘要类走非流式
    if intent in ("study_plan", "summarize"):
        handler = INTENT_DISPATCH_TABLE.get(intent)
        if not handler:
            return {"error": f"未知意图: {intent}"}
        return handler(req.message)

    # 对话/RAG/试卷类走流式
    handler = INTENT_DISPATCH_TABLE.get(intent, INTENT_DISPATCH_TABLE["chat"])

    async def generate():
        try:
            async for chunk in handler(req.message, req.session_id, req.history):
                yield chunk
            yield "[DONE]"
        except Exception as e:
            yield json.dumps({"error": str(e)}, ensure_ascii=False)
            yield "[DONE]"

    return StreamingResponse(
        generate(),
        media_type="text/event-stream",
        headers={"Cache-Control": "no-cache", "X-Accel-Buffering": "no"},
    )


# ── 独立端点（向后兼容） ──────────────────────────────────────

@router.post("/chat/stream")
async def chat_stream(req: ChatRequest):
    """SSE 流式对话 — 原 Java 调用入口"""
    chain = get_chat_chain()

    async def generate():
        try:
            async for chunk in chain.astream(
                {"input": req.message},
                config={"configurable": {"session_id": req.session_id}},
            ):
                yield chunk
            yield "[DONE]"
        except Exception as e:
            yield json.dumps({"error": str(e)}, ensure_ascii=False)
            yield "[DONE]"

    return StreamingResponse(
        generate(),
        media_type="text/event-stream",
        headers={"Cache-Control": "no-cache", "X-Accel-Buffering": "no"},
    )


@router.post("/summarize")
async def summarize(req: SummarizeRequest):
    chain = get_summarize_chain()
    try:
        summary = chain.invoke({"content": req.content[:8000], "max_length": req.max_length})
        return {"summary": summary}
    except Exception as e:
        raise LLMException(f"摘要生成失败: {e}")


@router.post("/search")
async def search(req: SearchRequest):
    results = semantic_search(req.query, req.top_k)
    return {"results": results}


@router.post("/study-plan/generate")
async def generate_study_plan(req: StudyPlanRequest):
    chain = get_study_plan_chain()
    raw = chain.invoke({
        "goal": req.goal, "total_days": req.days, "minutes": req.daily_minutes,
    })
    json_match = re.search(r"\[.*\]", raw, re.DOTALL)
    if not json_match:
        return {"tasks": [{"title": "计划生成失败", "content": "请重试", "type": "阅读"}]}
    try:
        return {"tasks": json.loads(json_match.group())}
    except json.JSONDecodeError:
        return {"tasks": [{"title": "计划解析失败", "content": raw, "type": "阅读"}]}
