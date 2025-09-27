FROM eclipse-temurin:21-jre-jammy

WORKDIR /todoist

EXPOSE 8080
EXPOSE 8081

COPY todoist-server/target/todoist-server-1.0-SNAPSHOT.jar .
COPY todoist-server/config/prod.yml /etc/todoist/config.yml

CMD ["java", "-jar", "todoist-server-1.0-SNAPSHOT.jar", "server", "/etc/todoist/config.yml"]