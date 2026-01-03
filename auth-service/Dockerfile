# Use a base image with arm64 support (works on Apple Silicon)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/*.war /app/app.war
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.war"]
