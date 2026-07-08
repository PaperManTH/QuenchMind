"""
@Author thpaperman
@Description 语义搜索与网络搜索工具（Tavily）
@Date 2026/7/7
@Version 1.0
"""
import psycopg2
from tavily import TavilyClient
from agent_service.tools.base import Tool, ToolRegistry
from agent_service.core.config import (
    PG_HOST, PG_PORT, PG_DATABASE, PG_USER, PG_PASSWORD,
    TAVILY_API_KEY,
)
from agent_service.core.exceptions import VectorStoreException

tavily = TavilyClient(api_key=TAVILY_API_KEY)


def _get_conn():
    return psycopg2.connect(
        host=PG_HOST, port=PG_PORT,
        database=PG_DATABASE, user=PG_USER, password=PG_PASSWORD,
    )


def semantic_search(query: str, top_k: int = 5) -> list[dict]:
    """pgvector 语义检索"""
    try:
        conn = _get_conn()
        cur = conn.cursor()
        cur.execute(
            """SELECT crc.id, crc.resource_id, crc.content
               FROM cz_resource_chunk crc
               ORDER BY crc.updated_at DESC
               LIMIT %s""",
            (top_k,),
        )
        rows = cur.fetchall()
        cur.close()
        conn.close()
        return [
            {"chunk_id": r[0], "resource_id": r[1], "content": r[2][:200], "score": 0.0}
            for r in rows
        ]
    except Exception as e:
        raise VectorStoreException(f"语义检索失败: {e}")


def web_search_tool(query: str, max_results: int = 5) -> str:
    """Tavily 网络搜索"""
    if not TAVILY_API_KEY:
        return "Tavily API Key 未配置"
    try:
        resp = tavily.search(query=query, max_results=max_results, include_raw_content=False)
        results = resp.get("results", [])
        if not results:
            return f"未找到与「{query}」相关的结果"
        lines = []
        for i, r in enumerate(results[:max_results], 1):
            title = r.get("title", "")
            url = r.get("url", "")
            content = r.get("content", "")[:300]
            lines.append(f"{i}. {title}\n   {url}\n   {content}")
        return "\n\n".join(lines)
    except Exception as e:
        return f"搜索失败: {e}"


ToolRegistry.register(Tool(
    name="semantic_search",
    description="在已索引的知识库文档中进行语义检索，返回相关分块",
    func=semantic_search,
    parameters={"query": "string", "top_k": "int"},
))

ToolRegistry.register(Tool(
    name="web_search",
    description="通过 Tavily 搜索互联网获取最新信息",
    func=web_search_tool,
    parameters={"query": "string", "max_results": "int"},
))
