FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/skin-clear.jar skin-clear.jar
EXPOSE 8080
CMD ["java", "-jar", "skin-clear.jar"]