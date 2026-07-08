"""
@Author thpaperman
@Description Java 文件服务 HTTP 客户端 — 封装对 Java Resource 模块的 API 调用
@Date 2026/7/7
@Version 1.0
"""
import requests
from agent_service.core.config import JAVA_API_BASE
from agent_service.core.exceptions import JavaAPIException


class FileServiceClient:
    """Java Resource 模块的文件服务客户端"""

    def __init__(self, base_url: str = JAVA_API_BASE):
        self.base_url = base_url.rstrip("/")

    def get_resource(self, resource_id: int) -> dict:
        """获取资源信息"""
        try:
            resp = requests.get(f"{self.base_url}/resource/{resource_id}", timeout=10)
            resp.raise_for_status()
            return resp.json().get("data", {})
        except requests.RequestException as e:
            raise JavaAPIException(f"获取资源信息失败: {e}")

    def callback(self, resource_id: int, summary: str, chunk_count: int, error: str = None) -> bool:
        """处理完成后回调 Java 更新向量化状态"""
        body = {
            "summary": summary,
            "chunk_count": chunk_count,
            "vector_status": 2 if chunk_count > 0 else 3,
        }
        if error:
            body["error"] = error
        try:
            resp = requests.put(
                f"{self.base_url}/resource/callback/{resource_id}",
                json=body, timeout=30,
            )
            return resp.status_code == 200
        except requests.RequestException as e:
            raise JavaAPIException(f"回调 Java 失败: {e}")

    def search_resources(self, keyword: str, page: int = 1, size: int = 20) -> dict:
        """搜索资源列表"""
        try:
            resp = requests.get(
                f"{self.base_url}/resource/list",
                params={"keyword": keyword, "page": page, "size": size},
                timeout=10,
            )
            resp.raise_for_status()
            return resp.json().get("data", {})
        except requests.RequestException as e:
            raise JavaAPIException(f"搜索资源失败: {e}")


file_service = FileServiceClient()
