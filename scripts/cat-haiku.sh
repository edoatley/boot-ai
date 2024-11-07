#!/usr/bin/env bash

HOST="localhost"
PORT="8080"
SCHEME="http"
API_PATH="ai/cathaiku"

url="$SCHEME://$HOST:$PORT/$API_PATH"

echo ""

curl -s "$url" | jq -r '.poem'
