#!/bin/sh
set -e

if [ -f /.dockerenv ]; then
  if ! nc -z -w 3 127.0.0.1 5432 2>/dev/null; then
    echo ""
    echo "================================================================"
    echo " ERROR: PostgreSQL is not reachable at 127.0.0.1:5432 from this"
    echo " container. On Ubuntu, Postgres runs on the HOST. You must start"
    echo " the backend with HOST networking, for example:"
    echo ""
    echo "   docker stop ktc-backend 2>/dev/null; docker rm ktc-backend 2>/dev/null"
    echo "   docker run -d --name ktc-backend --restart unless-stopped \\"
    echo "     --network host \\"
    echo "     -e SPRING_PROFILES_ACTIVE=prod \\"
    echo "     kodson/ktc-backend:latest"
    echo ""
    echo " Or from the project directory: docker compose up -d --build"
    echo " (compose uses network_mode: host)"
    echo "================================================================"
    echo ""
    exit 1
  fi
fi

exec java -jar /app/app.jar "$@"
