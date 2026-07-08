"""
@Author thpaperman
@Description 健康检查脚本 — 供 K8s/Docker 探针使用
@Date 2026/7/7
@Version 1.0
"""
import sys
import requests

HOST = "127.0.0.1"
PORT = 5000


def check_health() -> bool:
    try:
        resp = requests.get(f"http://{HOST}:{PORT}/health", timeout=5)
        return resp.status_code == 200 and resp.json().get("status") == "ok"
    except Exception:
        return False


def check_ready() -> bool:
    try:
        resp = requests.get(f"http://{HOST}:{PORT}/ready", timeout=5)
        return resp.status_code == 200 and resp.json().get("ready", False)
    except Exception:
        return False


if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--ready":
        ok = check_ready()
    else:
        ok = check_health()
    sys.exit(0 if ok else 1)
