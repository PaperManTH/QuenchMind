#!/bin/bash
# 服务启动脚本
set -e

cd "$(dirname "$0")/.."

if [ ! -f .env ]; then
    echo "复制 .env_template → .env (请编辑配置)"
    cp .env_template .env
fi

if [ ! -d .venv ]; then
    echo "创建虚拟环境..."
    python -m venv .venv
fi

source .venv/Scripts/activate 2>/dev/null || source .venv/bin/activate

echo "安装依赖..."
pip install -q fastapi uvicorn langchain langchain-openai langchain-core pika psycopg2-binary minio requests python-dotenv

echo "启动 AI Engine..."
python -m agent_service.main
