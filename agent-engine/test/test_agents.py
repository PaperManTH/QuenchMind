"""
@Author thpaperman
@Description Agent 核心逻辑单元测试
@Date 2026/7/7
@Version 1.0
"""
import pytest
from unittest.mock import patch, MagicMock

from agent_service.core.agent import clear_history
from agent_service.tools.base import Tool, ToolRegistry
from agent_service.tools.data_analysis import analyze_text_stats, extract_keywords


class TestToolRegistry:
    def test_register_and_get(self):
        ToolRegistry._tools.clear()
        tool = Tool(name="test", description="测试工具", func=lambda x: x)
        ToolRegistry.register(tool)
        assert ToolRegistry.get("test") is tool

    def test_list_tools(self):
        ToolRegistry._tools.clear()
        ToolRegistry.register(Tool(name="a", description="A", func=lambda: None))
        ToolRegistry.register(Tool(name="b", description="B", func=lambda: None))
        assert len(ToolRegistry.list_tools()) == 2

    def test_get_descriptions(self):
        ToolRegistry._tools.clear()
        ToolRegistry.register(Tool(name="search", description="搜索工具", func=lambda: None))
        desc = ToolRegistry.get_descriptions()
        assert "search" in desc
        assert "搜索工具" in desc

    def test_get_nonexistent(self):
        ToolRegistry._tools.clear()
        assert ToolRegistry.get("nonexistent") is None


class TestDataAnalysis:
    def test_text_stats_empty(self):
        result = analyze_text_stats("")
        assert result == {"chars": 0, "lines": 0, "words": 0}

    def test_text_stats_single_line(self):
        result = analyze_text_stats("hello world")
        assert result["chars"] == 11
        assert result["lines"] == 1
        assert result["words"] == 2

    def test_text_stats_multi_line(self):
        result = analyze_text_stats("hello world\nfoo bar")
        assert result["chars"] == 19
        assert result["lines"] == 2
        assert result["words"] == 4

    def test_extract_keywords_empty(self):
        assert extract_keywords("") == []

    def test_extract_keywords_basic(self):
        words = extract_keywords("AI AI 学习 学习 学习 数据")
        assert words[0] == "学习"
        assert len(words) == 3


class TestAgentHistory:
    def test_clear_history(self):
        clear_history("test-session")
        clear_history("non-existent")


class TestIntentClassifier:
    def test_keyword_study_plan(self):
        from agent_service.api.middleware.intent import classify_intent
        assert classify_intent("帮我制定一个学习计划") == "study_plan"
        assert classify_intent("生成学习路线") == "study_plan"

    def test_keyword_mock_exam(self):
        from agent_service.api.middleware.intent import classify_intent
        assert classify_intent("帮我出题") == "mock_exam"
        assert classify_intent("生成一份模拟试卷") == "mock_exam"

    def test_keyword_summarize(self):
        from agent_service.api.middleware.intent import classify_intent
        assert classify_intent("帮我总结这篇文档") == "summarize"

    @patch("agent_service.api.middleware.intent._classify_chain")
    def test_fallback_to_llm(self, mock_chain):
        from agent_service.api.middleware.intent import classify_intent
        mock_chain.invoke.return_value = "chat"
        result = classify_intent("今天天气怎么样")
        assert result == "chat"

    def test_dispatch_table_coverage(self):
        from agent_service.api.middleware.intent import INTENT_DISPATCH_TABLE
        assert "chat" in INTENT_DISPATCH_TABLE
        assert "rag" in INTENT_DISPATCH_TABLE
        assert "study_plan" in INTENT_DISPATCH_TABLE
        assert "mock_exam" in INTENT_DISPATCH_TABLE
        assert "summarize" in INTENT_DISPATCH_TABLE
