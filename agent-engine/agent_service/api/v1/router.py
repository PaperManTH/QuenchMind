"""
@Author thpaperman
@Description 聚合所有 v1 路由
@Date 2026/7/7
@Version 1.0
"""
from fastapi import APIRouter
from agent_service.api.v1.agent import router as agent_router
from agent_service.api.v1.health import router as health_router

router = APIRouter()
router.include_router(health_router, tags=["health"])
router.include_router(agent_router, tags=["agent"])
