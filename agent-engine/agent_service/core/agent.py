"""
@Author thpaperman
@Description Agent Chain 管理 — 统一管理各意图对应的 LangChain Chain
@Date 2026/7/7
@Version 2.0
"""
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_core.chat_history import InMemoryChatMessageHistory

from agent_service.core.config import OPENAI_MODEL, OPENAI_API_KEY, OPENAI_BASE_URL
from agent_service.prompts.agent_prompts import (
    CHAT_SYSTEM_PROMPT,
    RAG_SYSTEM_PROMPT,
    SUMMARIZE_SYSTEM_PROMPT,
    STUDY_PLAN_SYSTEM_PROMPT,
    MOCK_EXAM_SYSTEM_PROMPT,
)

_store: dict[str, InMemoryChatMessageHistory] = {}


def _get_history(session_id: str) -> InMemoryChatMessageHistory:
    if session_id not in _store:
        _store[session_id] = InMemoryChatMessageHistory()
    return _store[session_id]


def _create_llm(temperature: float = 0.7, streaming: bool = True) -> ChatOpenAI:
    return ChatOpenAI(
        model=OPENAI_MODEL,
        api_key=OPENAI_API_KEY,
        base_url=OPENAI_BASE_URL,
        temperature=temperature,
        streaming=streaming,
    )


def _wrap_history(chain):
    return RunnableWithMessageHistory(
        chain, _get_history,
        input_messages_key="input",
        history_messages_key="history",
    )


def get_chat_chain():
    """普通对话"""
    llm = _create_llm(temperature=0.7, streaming=True)
    prompt = ChatPromptTemplate.from_messages([
        ("system", CHAT_SYSTEM_PROMPT),
        MessagesPlaceholder(variable_name="history"),
        ("human", "{input}"),
    ])
    return _wrap_history(prompt | llm | StrOutputParser())


def get_rag_chain():
    """RAG 检索增强对话"""
    llm = _create_llm(temperature=0.5, streaming=True)
    prompt = ChatPromptTemplate.from_messages([
        ("system", RAG_SYSTEM_PROMPT),
        MessagesPlaceholder(variable_name="history"),
        ("human", "{input}"),
    ])
    return _wrap_history(prompt | llm | StrOutputParser())


def get_exam_chain():
    """模拟试卷生成"""
    llm = _create_llm(temperature=0.8, streaming=True)
    prompt = ChatPromptTemplate.from_messages([
        ("system", MOCK_EXAM_SYSTEM_PROMPT),
        MessagesPlaceholder(variable_name="history"),
        ("human", "请根据以下要求生成试卷：{input}"),
    ])
    return _wrap_history(prompt | llm | StrOutputParser())


def get_summarize_chain():
    """文档摘要"""
    llm = _create_llm(temperature=0.3, streaming=False)
    prompt = ChatPromptTemplate.from_messages([
        ("system", SUMMARIZE_SYSTEM_PROMPT),
        ("human", "{content}"),
    ])
    return prompt | llm | StrOutputParser()


def get_study_plan_chain():
    """学习计划"""
    llm = _create_llm(temperature=0.7, streaming=False)
    prompt = ChatPromptTemplate.from_messages([
        ("system", STUDY_PLAN_SYSTEM_PROMPT),
        ("human", "{goal}"),
    ])
    return prompt | llm | StrOutputParser()


def clear_history(session_id: str):
    _store.pop(session_id, None)
