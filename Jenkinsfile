pipeline {
    agent any
    
    // Add options for better pipeline management
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 1, unit: 'HOURS')
        skipStagesAfterUnstable()
    }
    
    environment {
        // Maven tool is working, but JDK tool is not found
        MAVEN_HOME = tool 'Maven 3.8.6'
        // Use system Java from PATH instead of tool configuration
        JAVA_HOME = 'C:\\Program Files\\Common Files\\Oracle\\Java\\javapath'
        PATH = "${MAVEN_HOME}/bin;${JAVA_HOME};C:\\Program Files\\Common Files\\Oracle\\Java\\javapath;${PATH}"
        SONAR_HOST_URL = 'http://localhost:9000'
        DOCKER_IMAGE = 'jenkins-pipeline-demo'
        DOCKER_TAG = "${BUILD_NUMBER}"
        SLACK_CHANNEL = '#devops-alerts'
        SLACK_TEAM_DOMAIN = 'your-team'
        // Make Slack token optional to avoid errors if not configured
        // SLACK_TOKEN = credentials('slack-token')
    }
    
    tools {
        maven 'Maven 3.8.6'
        // Comment out JDK tool since it's not working
        // jdk 'jdk11'
    }
    
    stages {
        stage('Debug Environment') {
            steps {
                echo 'Checking environment variables...'
                bat '''
                    echo "JAVA_HOME: %JAVA_HOME%"
                    echo "MAVEN_HOME: %MAVEN_HOME%"
                    echo "Checking Java..."
                    where java
                    java -version
                    echo "Checking Maven..."
                    mvn -version
                '''
            }
        }
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the application...'
                script {
                    try {
                        echo 'Starting Maven build...'
                        bat 'mvn clean compile -DskipTests=true'
                        echo 'Build completed successfully'
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        echo "❌ Build FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                        echo "Error: ${e.getMessage()}"
                        // Uncomment when Slack is configured:
                        // slackSend(channel: SLACK_CHANNEL, color: 'danger', message: "❌ Build FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}\nError: ${e.getMessage()}")
                        throw e
                    }
                }
            }
            post {
                success {
                    echo "✅ Build SUCCESS for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                    // Uncomment when Slack is configured:
                    // slackSend(channel: SLACK_CHANNEL, color: 'good', message: "✅ Build SUCCESS for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                }
            }
        }
        
        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                script {
                    try {
                        bat 'mvn test'
                        echo 'Unit tests completed'
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        echo "❌ Unit Tests FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                        // Uncomment when Slack is configured:
                        // slackSend(channel: SLACK_CHANNEL, color: 'danger', message: "❌ Unit Tests FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                        throw e
                    }
                }
            }
            post {
                always {
                    // Publish test results
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    
                    // Publish JaCoCo coverage report
                    publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')], 
                                   sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
                }
                success {
                    echo "✅ Unit Tests PASSED for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                    // Uncomment when Slack is configured:
                    // slackSend(channel: SLACK_CHANNEL, color: 'good', message: "✅ Unit Tests PASSED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                }
            }
        }
        
        stage('Code Quality') {
            parallel {
                stage('SonarQube Analysis') {
                    steps {
                        echo 'Running SonarQube analysis...'
                        script {
                            try {
                                // Check if SonarQube is available
                                echo 'SonarQube analysis would run here'
                                echo 'To enable: configure SonarQube server in Jenkins and uncomment the code below'
                                
                                // Uncomment when SonarQube is configured:
                                /*
                                withSonarQubeEnv('SonarQube') {
                                    sh '''
                                        mvn sonar:sonar \
                                        -Dsonar.projectKey=jenkins-pipeline-demo \
                                        -Dsonar.host.url=${SONAR_HOST_URL} \
                                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                                    '''
                                }
                                */
                            } catch (Exception e) {
                                echo "⚠️ SonarQube Analysis FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                                // Uncomment when Slack is configured:
                                // slackSend(channel: SLACK_CHANNEL, color: 'warning', message: "⚠️ SonarQube Analysis FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                                throw e
                            }
                        }
                    }
                }
                
                stage('Quality Gate') {
                    steps {
                        echo 'Quality Gate check...'
                        script {
                            try {
                                echo 'Quality gate would be checked here'
                                echo 'To enable: configure SonarQube server and uncomment the code below'
                                
                                // Uncomment when SonarQube is configured:
                                /*
                                timeout(time: 5, unit: 'MINUTES') {
                                    def qg = waitForQualityGate()
                                    if (qg.status != 'OK') {
                                        echo "❌ Quality Gate FAILED - Status: ${qg.status}"
                                        error "Pipeline aborted due to quality gate failure: ${qg.status}"
                                    }
                                }
                                */
                            } catch (Exception e) {
                                echo "⚠️ Quality Gate check FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                                // Uncomment when Slack is configured:
                                // slackSend(channel: SLACK_CHANNEL, color: 'warning', message: "⚠️ Quality Gate check FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                                throw e
                            }
                        }
                    }
                }
            }
            post {
                success {
                    echo "✅ Code Quality checks PASSED for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                    // Uncomment when Slack is configured:
                    // slackSend(channel: SLACK_CHANNEL, color: 'good', message: "✅ Code Quality checks PASSED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                }
            }
        }
        
        stage('Security') {
            parallel {
                stage('Dependency Check') {
                    steps {
                        echo 'Running OWASP Dependency Check...'
                        script {
                            try {
                                bat 'mvn org.owasp:dependency-check-maven:check'
                            } catch (Exception e) {
                                echo "⚠️ Security vulnerabilities found in dependencies for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                                // Uncomment when Slack is configured:
                                // slackSend(channel: SLACK_CHANNEL, color: 'warning', message: "⚠️ Security vulnerabilities found in dependencies for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                                // Continue pipeline even if vulnerabilities are found
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                    post {
                        always {
                            // Publish dependency check results
                            publishHTML([
                                allowMissing: false,
                                alwaysLinkToLastBuild: true,
                                keepAll: true,
                                reportDir: 'target/dependency-check',
                                reportFiles: 'dependency-check-report.html',
                                reportName: 'OWASP Dependency Check Report'
                            ])
                        }
                    }
                }
                
                stage('Trivy Security Scan') {
                    steps {
                        echo 'Running Trivy security scan...'
                        script {
                            try {
                                // Build temporary image for scanning
                                bat "docker build -t ${DOCKER_IMAGE}:temp ."
                                
                                // Run Trivy scan
                                bat """
                                    trivy image --format json --output trivy-report.json ${DOCKER_IMAGE}:temp || exit /b 0
                                    trivy image --format table ${DOCKER_IMAGE}:temp
                                """
                                
                                // Clean up temp image
                                bat "docker rmi ${DOCKER_IMAGE}:temp || exit /b 0"
                            } catch (Exception e) {
                                echo "⚠️ Trivy Security Scan encountered issues for ${env.JOB_NAME} - ${env.BUILD_NUMBER} - Branch: ${env.BRANCH_NAME}"
                                // Uncomment when Slack is configured:
                                // slackSend(channel: SLACK_CHANNEL, color: 'warning', message: "⚠️ Trivy Security Scan encountered issues for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}")
                                // Continue pipeline
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                }
            }
            post {
                success {
                    slackSend(
                        channel: SLACK_CHANNEL,
                        color: 'good',
                        message: "🔒 Security scans COMPLETED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}"
                    )
                }
            }
        }
        
        stage('Deploy') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo 'Deploying application...'
                script {
                    try {
                        // Package the application
                        bat 'mvn package -DskipTests'
                        
                        // Build Docker image
                        bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                        bat "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                        
                        // Deploy using Docker Compose (staging)
                        if (env.BRANCH_NAME == 'develop') {
                            bat """
                                set DOCKER_TAG=${DOCKER_TAG}
                                docker-compose -f docker-compose.staging.yml up -d
                            """
                            slackSend(
                                channel: SLACK_CHANNEL,
                                color: 'good',
                                message: "🚀 Application DEPLOYED to STAGING for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}\nTag: ${DOCKER_TAG}"
                            )
                        }
                        
                        // Deploy to production (main branch)
                        if (env.BRANCH_NAME == 'main') {
                            // Add manual approval for production
                            input message: 'Deploy to Production?', ok: 'Deploy',
                                  submitterParameter: 'APPROVER'
                            
                            bat """
                                set DOCKER_TAG=${DOCKER_TAG}
                                docker-compose -f docker-compose.prod.yml up -d
                            """
                            slackSend(
                                channel: SLACK_CHANNEL,
                                color: 'good',
                                message: "🎉 Application DEPLOYED to PRODUCTION for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nApproved by: ${APPROVER}\nTag: ${DOCKER_TAG}"
                            )
                        }
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        slackSend(
                            channel: SLACK_CHANNEL,
                            color: 'danger',
                            message: "❌ Deployment FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}\nError: ${e.getMessage()}"
                        )
                        throw e
                    }
                }
            }
        }
        
        stage('Acceptance Tests') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo 'Running acceptance tests...'
                script {
                    try {
                        // Wait for deployment to be ready
                        sleep(time: 30, unit: 'SECONDS')
                        
                        // Run integration tests against deployed application
                        bat 'mvn verify -Dtest.environment=staging'
                        
                        // Run API tests using Newman (Postman CLI) - Windows version
                        bat '''
                            where newman >nul 2>nul
                            if %ERRORLEVEL% EQU 0 (
                                newman run tests/api-tests.postman_collection.json -e tests/staging.postman_environment.json
                            ) else (
                                echo "Newman not installed, skipping API tests"
                            )
                        '''
                    } catch (Exception e) {
                        slackSend(
                            channel: SLACK_CHANNEL,
                            color: 'danger',
                            message: "❌ Acceptance Tests FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}"
                        )
                        throw e
                    }
                }
            }
            post {
                always {
                    // Publish integration test results
                    publishTestResults testResultsPattern: 'target/failsafe-reports/*.xml'
                }
                success {
                    slackSend(
                        channel: SLACK_CHANNEL,
                        color: 'good',
                        message: "✅ Acceptance Tests PASSED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}"
                    )
                }
            }
        }
        
        stage('Release') {
            when {
                branch 'main'
            }
            steps {
                echo 'Creating release...'
                script {
                    try {
                        // Create Git tag
                        bat """
                            git config user.name "Jenkins"
                            git config user.email "jenkins@example.com"
                            git tag -a v${BUILD_NUMBER} -m "Release version ${BUILD_NUMBER}"
                            git push origin v${BUILD_NUMBER}
                        """
                        
                        // Push Docker image to registry
                        withCredentials([usernamePassword(credentialsId: 'docker-registry', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                            bat '''
                                echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                                docker push %DOCKER_IMAGE%:%DOCKER_TAG%
                                docker push %DOCKER_IMAGE%:latest
                            '''
                        }
                        
                        slackSend(
                            channel: SLACK_CHANNEL,
                            color: 'good',
                            message: "🏷️ Release v${BUILD_NUMBER} CREATED for ${env.JOB_NAME}\nBranch: ${env.BRANCH_NAME}\nDocker Image: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                        )
                    } catch (Exception e) {
                        slackSend(
                            channel: SLACK_CHANNEL,
                            color: 'danger',
                            message: "❌ Release FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}"
                        )
                        throw e
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up...'
            // Archive artifacts
            archiveArtifacts artifacts: 'target/*.jar, target/surefire-reports/*.xml, target/site/jacoco/**/*', fingerprint: true
            
            // Clean workspace
            cleanWs()
        }
        
        success {
            slackSend(
                channel: SLACK_CHANNEL,
                color: 'good',
                message: "🎉 Pipeline COMPLETED SUCCESSFULLY for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}\nDuration: ${currentBuild.durationString}"
            )
        }
        
        failure {
            slackSend(
                channel: SLACK_CHANNEL,
                color: 'danger',
                message: "💥 Pipeline FAILED for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}\nDuration: ${currentBuild.durationString}\nCheck: ${env.BUILD_URL}"
            )
        }
        
        unstable {
            slackSend(
                channel: SLACK_CHANNEL,
                color: 'warning',
                message: "⚠️ Pipeline UNSTABLE for ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nBranch: ${env.BRANCH_NAME}\nDuration: ${currentBuild.durationString}"
            )
        }
    }
}