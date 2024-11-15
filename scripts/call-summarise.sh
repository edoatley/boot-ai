#!/usr/bin/env bash

TOPIC=${1:-"Kafka Brokers"}
HOST="localhost"
PORT="8080"
SCHEME="http"
APIPATH="api/ai/summarize"
url="$SCHEME://$HOST:$PORT/$APIPATH"

echo ""

curl -s -G "$url" --data-urlencode "topic=$TOPIC" | jq -r .summary