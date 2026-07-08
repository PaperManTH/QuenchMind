"""
@Author thpaperman
@Description API 响应体模型
@Date 2026/7/7
@Version 1.0
"""
from typing import Any
from pydantic import BaseModel, Field


class HealthResponse(BaseModel):
    status: str = Field(default="ok")
    version: str = Field(default="1.0.0")


class SummarizeResponse(BaseModel):
    summary: str = Field(..., description="摘要文本")


class SearchResult(BaseModel):
    chunk_id: int = Field(..., description="分块 ID")
    resource_id: int = Field(..., description="资源 ID")
    content: str = Field(..., description="分块内容")
    score: float = Field(..., description="相似度分数")


class SearchResponse(BaseModel):
    results: list[SearchResult] = Field(default_factory=list)


class StudyPlanTask(BaseModel):
    title: str = Field(..., description="任务标题")
    content: str = Field(..., description="任务内容")
    type: str = Field(default="阅读", description="任务类型: 阅读/练习/复习/测验")


class StudyPlanResponse(BaseModel):
    tasks: list[StudyPlanTask] = Field(default_factory=list)


class ErrorResponse(BaseModel):
    error: str = Field(..., description="错误信息")
    code: int = Field(default=500, description="错误码")
    detail: str = Field(default="", description="详细信息")
