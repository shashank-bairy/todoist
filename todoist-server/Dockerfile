FROM alpine:3.18

WORKDIR /todoist

EXPOSE 8080
EXPOSE 8081

COPY target/todoist-server-1.0-SNAPSHOT.jar .
COPY config/prod.yml /etc/todoist/config.yml

CMD ["java", "-jar", "todoist-server-1.0-SNAPSHOT.jar", "server", "/etc/todoist/config.yml"]