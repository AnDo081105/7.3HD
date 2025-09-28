# Jenkins Pipeline Demo

A comprehensive Java Spring Boot application that demonstrates a complete CI/CD pipeline using Jenkins with multiple stages including Build, Unit Tests, Code Quality, Security, Deploy, Acceptance Tests, Release, and Slack notifications.

## 🚀 Features

- **RESTful API** with basic calculator operations
- **Comprehensive Testing** (Unit tests, Integration tests, API tests)
- **Code Quality** analysis with SonarQube
- **Security Scanning** with OWASP Dependency Check and Trivy
- **Containerized** deployment with Docker
- **CI/CD Pipeline** with Jenkins
- **Slack Integration** for build notifications

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker
- Jenkins with required plugins
- SonarQube server
- Slack workspace (for notifications)

## 🏗️ Project Structure

```
jenkins-pipeline-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── JenkinsPipelineDemoApplication.java
│   │   │   ├── controller/DemoController.java
│   │   │   └── service/CalculatorService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/example/demo/
│       │   ├── controller/DemoControllerTest.java
│       │   ├── service/CalculatorServiceTest.java
│       │   └── integration/ApplicationIntegrationTest.java
│       └── resources/
│           └── application-test.properties
├── tests/
│   ├── api-tests.postman_collection.json
│   └── staging.postman_environment.json
├── Dockerfile
├── docker-compose.staging.yml
├── docker-compose.prod.yml
├── Jenkinsfile
├── pom.xml
└── README.md
```

## 🔧 API Endpoints

### Health Check
- `GET /api/health` - Application health status

### Calculator Operations
- `GET /api/add?a={number}&b={number}` - Addition
- `GET /api/subtract?a={number}&b={number}` - Subtraction
- `GET /api/multiply?a={number}&b={number}` - Multiplication
- `GET /api/divide?a={number}&b={number}` - Division

### Example Requests

```bash
# Health check
curl http://localhost:8080/api/health

# Addition
curl "http://localhost:8080/api/add?a=5&b=3"

# Division
curl "http://localhost:8080/api/divide?a=15&b=3"
```

## 🏃‍♂️ Running the Application

### Local Development

```bash
# Clone and build
mvn clean compile

# Run tests
mvn test

# Start the application
mvn spring-boot:run

# Access the application
curl http://localhost:8080/api/health
```

### Docker

```bash
# Build the application
mvn package -DskipTests

# Build Docker image
docker build -t jenkins-pipeline-demo:latest .

# Run container
docker run -p 8080:8080 jenkins-pipeline-demo:latest
```

### Docker Compose

```bash
# Staging environment
docker-compose -f docker-compose.staging.yml up -d

# Production environment
docker-compose -f docker-compose.prod.yml up -d
```

## 🔄 Jenkins Pipeline

The Jenkins pipeline includes the following stages:

### 1. **Build**
- Compiles the Java source code
- Validates the project structure
- Prepares artifacts for testing

### 2. **Unit Tests**
- Runs JUnit tests
- Generates code coverage reports with JaCoCo
- Publishes test results

### 3. **Code Quality**
- **SonarQube Analysis**: Static code analysis
- **Quality Gate**: Enforces quality standards

### 4. **Security**
- **OWASP Dependency Check**: Scans for vulnerable dependencies
- **Trivy Scan**: Container security scanning

### 5. **Deploy**
- Builds Docker image
- Deploys to staging/production using Docker Compose
- Includes manual approval for production deployments

### 6. **Acceptance Tests**
- Runs integration tests against deployed application
- Executes API tests using Newman/Postman collections

### 7. **Release**
- Creates Git tags for releases
- Pushes Docker images to registry
- Updates version information

### 8. **Slack Alerts**
- Sends notifications for each stage
- Includes success/failure status
- Provides build links and details

## ⚙️ Jenkins Setup

### Required Jenkins Plugins
- Pipeline plugin
- Docker plugin
- SonarQube Scanner plugin
- JaCoCo plugin
- Slack Notification plugin
- Blue Ocean (recommended)

### Environment Variables
Set these in Jenkins global configuration or pipeline:

```groovy
SONAR_HOST_URL = 'http://your-sonarqube-server:9000'
SLACK_CHANNEL = '#your-devops-channel'
SLACK_TEAM_DOMAIN = 'your-team'
DOCKER_REGISTRY = 'your-docker-registry'
```

### Credentials
Configure these credentials in Jenkins:
- `slack-token`: Slack bot token
- `docker-registry`: Docker registry credentials
- `sonar-token`: SonarQube authentication token

## 📊 Code Quality & Security

### SonarQube Configuration
The project includes SonarQube analysis with:
- Code coverage reporting
- Code smells detection
- Security hotspot identification
- Maintainability metrics

### Security Scanning
- **OWASP Dependency Check**: Identifies known vulnerabilities in dependencies
- **Trivy**: Scans Docker images for vulnerabilities

## 🧪 Testing Strategy

### Unit Tests
- Service layer testing with JUnit 5
- Mocking with Mockito
- High code coverage target (>80%)

### Integration Tests
- Full application context testing
- REST API endpoint validation
- Database interaction testing (if applicable)

### API Tests
- Postman collection for acceptance testing
- Automated API validation
- Cross-environment testing

## 📈 Monitoring & Observability

The application includes Spring Boot Actuator endpoints:
- `/actuator/health` - Health checks
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application information

## 🔧 Configuration

### Application Properties
- Environment-specific configurations
- Logging levels
- Actuator endpoint exposure
- Profile management

### Docker Configuration
- Multi-stage builds for optimization
- Security best practices
- Health checks
- Resource limits

## 🚀 Deployment Environments

### Staging
- Automatic deployment from `develop` branch
- Port 8080 exposed
- Development configurations

### Production
- Manual approval required
- Port 80 exposed
- Production-optimized settings
- Resource limits enforced

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team via Slack
- Check the Jenkins build logs for troubleshooting

---

**Happy Building! 🎉**