"""
@Author thpaperman
@Description 工具基类与注册器
@Date 2026/7/7
@Version 1.0
"""
from typing import Any, Callable
from dataclasses import dataclass, field


@dataclass
class Tool:
    name: str
    description: str
    func: Callable
    parameters: dict[str, Any] = field(default_factory=dict)


class ToolRegistry:
    """工具注册表"""
    _tools: dict[str, Tool] = {}

    @classmethod
    def register(cls, tool: Tool):
        cls._tools[tool.name] = tool

    @classmethod
    def get(cls, name: str) -> Tool | None:
        return cls._tools.get(name)

    @classmethod
    def list_tools(cls) -> list[Tool]:
        return list(cls._tools.values())

    @classmethod
    def get_descriptions(cls) -> str:
        return "\n".join(f"- {t.name}: {t.description}" for t in cls._tools.values())
