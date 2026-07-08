FROM python:3.11-slim

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

ENV AI_ENGINE_HOST=0.0.0.0
ENV AI_ENGINE_PORT=5000

EXPOSE 5000

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
    CMD python scripts/healthcheck.py || exit 1

CMD ["python", "-m", "agent_service.main"]
