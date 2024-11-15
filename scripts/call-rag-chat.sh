#!/usr/bin/env bash

QUESTION=${1:-"What is a broker in Kafka?"}
HOST="localhost"
PORT="8080"
SCHEME="http"
APIPATH="api/ai/rag/chat"
url="$SCHEME://$HOST:$PORT/$APIPATH"

echo ""

fold -w 80 -s <<< $(curl -s -G "$url" --data-urlencode "query=$QUESTION" | jq -r .answer)
