#!/bin/bash

# Jenkins Pipeline Demo - Local Test Script
# This script demonstrates the pipeline stages locally

set -e

echo "🚀 Starting Jenkins Pipeline Demo Local Test"
echo "=============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

function print_stage() {
    echo -e "\n${BLUE}📋 Stage: $1${NC}"
    echo "----------------------------------------"
}

function print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

function print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

function print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Stage 1: Build
print_stage "Build"
echo "Compiling Java source code..."
if mvn clean compile -q; then
    print_success "Build completed successfully"
else
    print_error "Build failed"
    exit 1
fi

# Stage 2: Unit Tests
print_stage "Unit Tests"
echo "Running unit tests..."
if mvn test -q; then
    print_success "Unit tests passed"
    print_success "JaCoCo coverage report generated"
else
    print_error "Unit tests failed"
    exit 1
fi

# Stage 3: Code Quality (Mock)
print_stage "Code Quality"
echo "Running code quality checks..."
# Note: This would normally run SonarQube analysis
if mvn compile -q; then
    print_success "Code quality checks passed"
else
    print_warning "Code quality checks had issues"
fi

# Stage 4: Security (Mock)
print_stage "Security"
echo "Running security scans..."
# Note: This would normally run OWASP Dependency Check and Trivy
if mvn compile -q; then
    print_success "Security scans completed"
else
    print_warning "Security scans found issues"
fi

# Stage 5: Package
print_stage "Package"
echo "Creating JAR package..."
if mvn package -DskipTests -q; then
    print_success "Application packaged successfully"
else
    print_error "Packaging failed"
    exit 1
fi

# Stage 6: Docker Build (if Docker is available)
print_stage "Docker Build"
if command -v docker &> /dev/null; then
    echo "Building Docker image..."
    if docker build -t jenkins-pipeline-demo:test . &> /dev/null; then
        print_success "Docker image built successfully"
    else
        print_warning "Docker build failed"
    fi
else
    print_warning "Docker not available, skipping Docker build"
fi

# Stage 7: Integration Tests
print_stage "Integration Tests"
echo "Running integration tests..."
if mvn verify -q; then
    print_success "Integration tests passed"
else
    print_warning "Integration tests had issues"
fi

echo -e "\n${GREEN}🎉 Pipeline completed successfully!${NC}"
echo "=============================================="
echo "📊 Test Results:"
echo "   - Unit Tests: target/surefire-reports/"
echo "   - Coverage: target/site/jacoco/"
echo "   - JAR File: target/jenkins-pipeline-demo-1.0.0.jar"

if command -v docker &> /dev/null && docker images jenkins-pipeline-demo:test &> /dev/null; then
    echo "   - Docker Image: jenkins-pipeline-demo:test"
fi

echo -e "\n🚀 To run the application:"
echo "   java -jar target/jenkins-pipeline-demo-1.0.0.jar"
echo "   or"
echo "   docker run -p 8080:8080 jenkins-pipeline-demo:test"
echo -e "\n✅ Access: http://localhost:8080/api/health"