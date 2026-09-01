#!/bin/bash

set -e

echo "Starting PostgreSQL..."
docker compose \
  -f docker-compose.yml \
  -f docker-compose.local.yml \
  up -d exambyteDB

echo "PostgreSQL is running."

echo "Starting Spring Boot with the local profile..."
./gradlew bootRun --args='--spring.profiles.active=local'
```
