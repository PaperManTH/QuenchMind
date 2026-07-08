"""
@Author thpaperman
@Description 数据分析工具
@Date 2026/7/7
@Version 1.0
"""
import json
from agent_service.tools.base import Tool, ToolRegistry


def analyze_text_stats(text: str) -> dict:
    """文本统计分析"""
    chars = len(text)
    lines = text.count("\n") + 1 if text else 0
    words = len(text.split()) if text else 0
    return {"chars": chars, "lines": lines, "words": words}


def extract_keywords(text: str, top_n: int = 10) -> list[str]:
    """简单关键词提取（基于词频，占位，后续可替换为 NLP 方案）"""
    if not text:
        return []
    # 简单实现：按长度过滤 + 去重
    words = text.replace("\n", " ").split()
    freq: dict[str, int] = {}
    for w in words:
        if len(w) >= 2:
            freq[w] = freq.get(w, 0) + 1
    sorted_words = sorted(freq.items(), key=lambda x: x[1], reverse=True)
    return [w for w, _ in sorted_words[:top_n]]


ToolRegistry.register(Tool(
    name="text_stats",
    description="分析文本的字符数、行数、词数等统计信息",
    func=analyze_text_stats,
    parameters={"text": "string"},
))

ToolRegistry.register(Tool(
    name="extract_keywords",
    description="从文本中提取高频关键词",
    func=extract_keywords,
    parameters={"text": "string", "top_n": "int"},
))
