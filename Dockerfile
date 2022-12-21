FROM openjdk:21-slim
ARG JAR_FILE=target/BotV2-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8081:8081
ENTRYPOINT ["java","-jar","app.jar"]

