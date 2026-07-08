"""
@Author thpaperman
@Description 健康检查接口
@Date 2026/7/7
@Version 1.0
"""
from fastapi import APIRouter

router = APIRouter()


@router.get("/health")
async def health():
    return {"status": "ok", "version": "1.0.0"}


@router.get("/ready")
async def ready():
    """就绪探针：检查关键依赖是否可用"""
    from agent_service.core.config import OPENAI_API_KEY
    checks = {"llm": bool(OPENAI_API_KEY)}
    all_ok = all(checks.values())
    return {"ready": all_ok, "checks": checks}
