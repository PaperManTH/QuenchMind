"""
@Author thpaperman
@Description API 端点测试
@Date 2026/7/7
@Version 1.0
"""
import json
import pytest
from fastapi.testclient import TestClient
from unittest.mock import patch, MagicMock

from agent_service.main import app

client = TestClient(app)


class TestHealth:
    def test_health_returns_ok(self):
        resp = client.get("/health")
        assert resp.status_code == 200
        data = resp.json()
        assert data["status"] == "ok"
        assert "version" in data

    def test_ready_endpoint(self):
        resp = client.get("/ready")
        assert resp.status_code == 200
        data = resp.json()
        assert "ready" in data
        assert "checks" in data


class TestSummarize:
    @patch("agent_service.api.v1.agent.get_summarize_chain")
    def test_summarize_accepts_content(self, mock_chain):
        mock_chain.return_value.invoke.return_value = "测试摘要内容"
        resp = client.post("/api/v1/summarize", json={
            "content": "这是一段测试文本",
            "max_length": 100,
        })
        assert resp.status_code == 200
        assert "summary" in resp.json()

    @patch("agent_service.api.v1.agent.get_summarize_chain")
    def test_summarize_default_max_length(self, mock_chain):
        mock_chain.return_value.invoke.return_value = "摘要"
        resp = client.post("/api/v1/summarize", json={"content": "测试内容"})
        assert resp.status_code == 200


class TestSearch:
    @patch("agent_service.api.v1.agent.semantic_search")
    def test_search_accepts_query(self, mock_search):
        mock_search.return_value = [
            {"chunk_id": 1, "resource_id": 10, "content": "结果1", "score": 0.9},
        ]
        resp = client.post("/api/v1/search", json={"query": "机器学习", "top_k": 3})
        assert resp.status_code == 200
        data = resp.json()
        assert "results" in data
        assert len(data["results"]) == 1

    @patch("agent_service.api.v1.agent.semantic_search")
    def test_search_default_top_k(self, mock_search):
        mock_search.return_value = []
        resp = client.post("/api/v1/search", json={"query": "深度学习"})
        assert resp.status_code == 200
        assert resp.json()["results"] == []


class TestStudyPlan:
    @patch("agent_service.api.v1.agent.get_study_plan_chain")
    def test_generate_plan_returns_tasks(self, mock_chain):
        mock_chain.return_value.invoke.return_value = json.dumps([
            {"title": "Python 基础", "content": "学习变量和类型", "type": "阅读"},
            {"title": "练习", "content": "完成课后习题", "type": "练习"},
        ])
        resp = client.post("/api/v1/study-plan/generate", json={
            "goal": "学习 Python 基础",
            "days": 3,
            "daily_minutes": 30,
        })
        assert resp.status_code == 200
        data = resp.json()
        assert "tasks" in data
        assert len(data["tasks"]) == 2

    def test_generate_plan_validation_min_days(self):
        resp = client.post("/api/v1/study-plan/generate", json={
            "goal": "测试",
            "days": 0,
        })
        assert resp.status_code == 422

    @patch("agent_service.api.v1.agent.get_study_plan_chain")
    def test_generate_plan_no_json_found(self, mock_chain):
        mock_chain.return_value.invoke.return_value = "无法生成计划"
        resp = client.post("/api/v1/study-plan/generate", json={
            "goal": "学习",
            "days": 1,
        })
        assert resp.status_code == 200
        assert resp.json()["tasks"][0]["title"] == "计划生成失败"


class TestChatStream:
    @patch("agent_service.api.v1.agent.get_chat_chain")
    def test_chat_stream_returns_sse(self, mock_chain):
        async def mock_astream(*args, **kwargs):
            yield "你好"
        mock_chain.return_value.astream = mock_astream

        resp = client.post("/api/v1/chat/stream", json={
            "session_id": "test-001",
            "message": "你好",
        })
        assert resp.status_code == 200
        assert "text/event-stream" in resp.headers.get("content-type", "")


class TestUnifiedChat:
    @patch("agent_service.api.v1.agent.classify_intent")
    @patch("agent_service.api.v1.agent.get_chat_chain")
    def test_route_to_chat(self, mock_chain, mock_classify):
        mock_classify.return_value = "chat"
        async def mock_astream(*a, **kw):
            yield "回复"
        mock_chain.return_value.astream = mock_astream

        resp = client.post("/api/v1/chat", json={
            "session_id": "s1", "message": "什么是机器学习",
        })
        assert resp.status_code == 200

    @patch("agent_service.api.v1.agent.classify_intent")
    @patch.dict("agent_service.api.v1.agent.INTENT_DISPATCH_TABLE", {
        "study_plan": lambda msg: {"tasks": [{"title": "模块1", "content": "内容", "type": "阅读"}]},
    })
    def test_route_to_study_plan(self, mock_classify):
        mock_classify.return_value = "study_plan"
        resp = client.post("/api/v1/chat", json={
            "session_id": "s1", "message": "帮我制定学习计划",
        })
        assert resp.status_code == 200
        data = resp.json()
        assert "tasks" in data
        assert len(data["tasks"]) == 1

    @patch("agent_service.api.v1.agent.classify_intent")
    @patch.dict("agent_service.api.v1.agent.INTENT_DISPATCH_TABLE", {
        "summarize": lambda msg: {"summary": "摘要内容"},
    })
    def test_route_to_summarize(self, mock_classify):
        mock_classify.return_value = "summarize"
        resp = client.post("/api/v1/chat", json={
            "session_id": "s1", "message": "帮我总结这段内容",
        })
        assert resp.status_code == 200
        assert "summary" in resp.json()

    @patch("agent_service.api.v1.agent.classify_intent")
    @patch("agent_service.api.v1.agent.get_rag_chain")
    def test_route_to_rag(self, mock_chain, mock_classify):
        mock_classify.return_value = "rag"
        async def mock_astream(*a, **kw):
            yield "RAG 回复"
        mock_chain.return_value.astream = mock_astream

        resp = client.post("/api/v1/chat", json={
            "session_id": "s1", "message": "根据知识库回答",
        })
        assert resp.status_code == 200
        assert "text/event-stream" in resp.headers.get("content-type", "")

    @patch("agent_service.api.v1.agent.classify_intent")
    @patch("agent_service.api.v1.agent.get_exam_chain")
    def test_route_to_exam(self, mock_chain, mock_classify):
        mock_classify.return_value = "mock_exam"
        async def mock_astream(*a, **kw):
            yield "试卷生成中"
        mock_chain.return_value.astream = mock_astream

        resp = client.post("/api/v1/chat", json={
            "session_id": "s1", "message": "给我出一份数学试卷",
        })
        assert resp.status_code == 200
