FROM adoptopenjdk/openjdk11:ubi
VOLUME /tmp
ADD ./target/FNSI_Cache-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]