FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/ktc-backend.jar app.jar
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh && apk add --no-cache netcat-openbsd

EXPOSE 8082

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["/docker-entrypoint.sh"]
