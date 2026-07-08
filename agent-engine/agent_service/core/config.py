"""
@Author thpaperman
@Description 配置管理 — 从 .env 文件和环境变量加载配置
@Date 2026/7/7
@Version 1.0
"""
import os
from pathlib import Path


def _load_env():
    """加载 .env 文件到 os.environ"""
    env_file = Path(__file__).resolve().parent.parent.parent / ".env"
    if not env_file.exists():
        return
    with open(env_file, encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#") or "=" not in line:
                continue
            key, _, val = line.partition("=")
            key, val = key.strip(), val.strip().strip('"').strip("'")
            if key not in os.environ:
                os.environ[key] = val


_load_env()


def _env(key, default=None):
    return os.environ.get(key, default)


# 服务配置
HOST = _env("AI_ENGINE_HOST", "0.0.0.0")
PORT = int(_env("AI_ENGINE_PORT", "5000"))

# LLM 配置
LLM_PROVIDER = _env("LLM_PROVIDER", "openai")
OPENAI_API_KEY = _env("OPENAI_API_KEY", "")
OPENAI_BASE_URL = _env("OPENAI_BASE_URL", "https://api.openai.com/v1")
OPENAI_MODEL = _env("OPENAI_MODEL", "gpt-4o-mini")

# Java 后端
JAVA_API_BASE = _env("JAVA_API_BASE", "http://localhost:8080")

# PostgreSQL
PG_HOST = _env("PG_HOST", "localhost")
PG_PORT = int(_env("PG_PORT", "5432"))
PG_DATABASE = _env("PG_DATABASE", "cuizhi")
PG_USER = _env("PG_USER", "postgres")
PG_PASSWORD = _env("PG_PASSWORD", "postgres")

# RabbitMQ
RABBITMQ_HOST = _env("RABBITMQ_HOST", "localhost")
RABBITMQ_PORT = int(_env("RABBITMQ_PORT", "5672"))
RABBITMQ_USER = _env("RABBITMQ_USER", "guest")
RABBITMQ_PASS = _env("RABBITMQ_PASS", "guest")

# MinIO
MINIO_ENDPOINT = _env("MINIO_ENDPOINT", "localhost:9000")
MINIO_ACCESS_KEY = _env("MINIO_ACCESS_KEY", "minioadmin")
MINIO_SECRET_KEY = _env("MINIO_SECRET_KEY", "minioadmin")
MINIO_BUCKET = _env("MINIO_BUCKET", "cuizhi")

# Tavily 搜索
TAVILY_API_KEY = _env("TAVILY_API_KEY", "")

