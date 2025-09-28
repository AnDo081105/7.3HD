FROM openjdk:11-jre-slim

LABEL maintainer="your-email@example.com"
LABEL description="Jenkins Pipeline Demo Application"

# Create app directory
WORKDIR /app

# Copy the JAR file
COPY target/jenkins-pipeline-demo-*.jar app.jar

# Create a non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser && \
    chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]