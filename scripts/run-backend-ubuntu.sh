#!/usr/bin/env bash
# Postgres on Ubuntu host; credentials in application-prod.yml inside the image.
set -euo pipefail

IMAGE="${KTC_BACKEND_IMAGE:-kodson/ktc-backend:latest}"
NAME="${KTC_CONTAINER_NAME:-ktc-backend}"

docker stop "$NAME" 2>/dev/null || true
docker rm "$NAME" 2>/dev/null || true

docker run -d \
  --name "$NAME" \
  --restart unless-stopped \
  --network host \
  -e SPRING_PROFILES_ACTIVE=prod \
  "$IMAGE"

echo "Started $NAME with --network host. Logs: docker logs -f $NAME"
