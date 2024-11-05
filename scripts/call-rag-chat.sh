#!/usr/bin/env bash

QUESTION=${1:-"What is a broker in Kafka?"}
HOST="localhost"
PORT="8080"
SCHEME="http"

q=$(python3 -c 'import urllib.parse, sys; print(urllib.parse.quote_plus(sys.argv[1]))' "$QUESTION")
url="$SCHEME://$HOST:$PORT/rag/chat?query=$q"

echo "Calling $url"
curl -s "$url" | jq