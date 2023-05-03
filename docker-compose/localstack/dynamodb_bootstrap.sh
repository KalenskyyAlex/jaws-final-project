#!/usr/bin/env bash

set -euo pipefail

# enable debug
# set -x

echo "configuring secret manager"
echo "==================="
LOCALSTACK_HOST=localhost


create_dynamodb_table() {
    local IN_FILE=$1
    awslocal dynamodb create-table --cli-input-json $IN_FILE  --endpoint-url=http://${LOCALSTACK_HOST}:4566
}

create_dynamodb_table file:///docker-entrypoint-initaws.d/create-tutorials.json