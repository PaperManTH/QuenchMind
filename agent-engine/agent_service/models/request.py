"""
@Author thpaperman
@Description API 请求体模型
@Date 2026/7/7
@Version 1.0
"""
from pydantic import BaseModel, Field


class ChatRequest(BaseModel):
    session_id: str = Field(default="default", description="会话 ID")
    message: str = Field(..., description="用户消息")
    history: list[dict] = Field(default_factory=list, description="历史对话")


class SummarizeRequest(BaseModel):
    content: str = Field(..., description="待摘要文本")
    max_length: int = Field(default=500, description="最大摘要长度")


class SearchRequest(BaseModel):
    query: str = Field(..., description="检索关键词")
    top_k: int = Field(default=5, ge=1, le=20, description="返回数量")


class StudyPlanRequest(BaseModel):
    goal: str = Field(..., description="学习目标")
    days: int = Field(default=7, ge=1, le=365, description="学习天数")
    daily_minutes: int = Field(default=60, ge=10, le=480, description="每天学习时长(分钟)")
    resource_ids: list[int] = Field(default_factory=list, description="关联资源 ID")
