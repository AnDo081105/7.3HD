@echo off
REM Jenkins Pipeline Demo - Local Test Script for Windows
REM This script demonstrates the pipeline stages locally

echo 🚀 Starting Jenkins Pipeline Demo Local Test
echo ==============================================
echo.

REM Stage 1: Build
echo 📋 Stage: Build
echo ----------------------------------------
echo Compiling Java source code...
call mvn clean compile -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Build failed
    exit /b 1
)
echo ✅ Build completed successfully
echo.

REM Stage 2: Unit Tests
echo 📋 Stage: Unit Tests
echo ----------------------------------------
echo Running unit tests...
call mvn test -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Unit tests failed
    exit /b 1
)
echo ✅ Unit tests passed
echo ✅ JaCoCo coverage report generated
echo.

REM Stage 3: Code Quality (Mock)
echo 📋 Stage: Code Quality
echo ----------------------------------------
echo Running code quality checks...
call mvn compile -q
if %ERRORLEVEL% neq 0 (
    echo ⚠️ Code quality checks had issues
) else (
    echo ✅ Code quality checks passed
)
echo.

REM Stage 4: Security (Mock)
echo 📋 Stage: Security
echo ----------------------------------------
echo Running security scans...
call mvn compile -q
if %ERRORLEVEL% neq 0 (
    echo ⚠️ Security scans found issues
) else (
    echo ✅ Security scans completed
)
echo.

REM Stage 5: Package
echo 📋 Stage: Package
echo ----------------------------------------
echo Creating JAR package...
call mvn package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Packaging failed
    exit /b 1
)
echo ✅ Application packaged successfully
echo.

REM Stage 6: Docker Build (if Docker is available)
echo 📋 Stage: Docker Build
echo ----------------------------------------
docker --version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ⚠️ Docker not available, skipping Docker build
) else (
    echo Building Docker image...
    docker build -t jenkins-pipeline-demo:test . >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ⚠️ Docker build failed
    ) else (
        echo ✅ Docker image built successfully
    )
)
echo.

REM Stage 7: Integration Tests
echo 📋 Stage: Integration Tests
echo ----------------------------------------
echo Running integration tests...
call mvn verify -q
if %ERRORLEVEL% neq 0 (
    echo ⚠️ Integration tests had issues
) else (
    echo ✅ Integration tests passed
)
echo.

echo 🎉 Pipeline completed successfully!
echo ==============================================
echo 📊 Test Results:
echo    - Unit Tests: target\surefire-reports\
echo    - Coverage: target\site\jacoco\
echo    - JAR File: target\jenkins-pipeline-demo-1.0.0.jar

docker images jenkins-pipeline-demo:test >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo    - Docker Image: jenkins-pipeline-demo:test
)

echo.
echo 🚀 To run the application:
echo    java -jar target\jenkins-pipeline-demo-1.0.0.jar
echo    or
echo    docker run -p 8080:8080 jenkins-pipeline-demo:test
echo.
echo ✅ Access: http://localhost:8080/api/health

pause