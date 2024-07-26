FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/skinclear-backend-0.0.1-SNAPSHOT.jar skin-clear.jar
EXPOSE 8080
CMD ["java", "-jar", "skin-clear.jar"]