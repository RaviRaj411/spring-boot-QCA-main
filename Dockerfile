FROM openjdk:11
EXPOSE 8080
ADD ./target/QCA-BACK-0.0.1.jar app.jar

ENTRYPOINT [ "java","-jar", "/app.jar"] 