FROM openjdk:21-jdk

COPY target/KodsonApi.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "KodsonApi.jar"]
