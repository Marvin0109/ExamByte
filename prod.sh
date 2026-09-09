#!/bin/bash

set -e

echo "Pulling latest changes..."
git pull

echo "Building application..."
./gradlew clean build

echo "Rebuilding Docker container..."
docker compose up -d --build

echo "Deployment finished!"
docker compose ps
