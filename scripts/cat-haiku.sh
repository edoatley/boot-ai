#!/usr/bin/env bash

QUESTION=${1:-"What is a broker in Kafka?"}
HOST="localhost"
PORT="8080"
SCHEME="http"
APIPATH="ai/cathaiku"

url="$SCHEME://$HOST:$PORT/$APIPATH"

curl -s "$url" | jq -r '.poem'
