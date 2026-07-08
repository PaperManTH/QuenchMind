"""
@Author thpaperman
@Description 自定义异常类
@Date 2026/7/7
@Version 1.0
"""


class AgentEngineException(Exception):
    """AI 引擎基础异常"""
    def __init__(self, message: str, code: int = 500):
        self.message = message
        self.code = code
        super().__init__(message)


class LLMException(AgentEngineException):
    """LLM 调用异常"""
    def __init__(self, message: str):
        super().__init__(message, code=502)


class DocumentProcessException(AgentEngineException):
    """文档处理异常"""
    def __init__(self, message: str):
        super().__init__(message, code=500)


class VectorStoreException(AgentEngineException):
    """向量存储异常"""
    def __init__(self, message: str):
        super().__init__(message, code=500)


class JavaAPIException(AgentEngineException):
    """Java 后端 API 调用异常"""
    def __init__(self, message: str, code: int = 502):
        super().__init__(message, code=code)
