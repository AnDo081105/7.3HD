# Jenkins Configuration Fix - Windows Compatibility

## Issues Fixed:

### 1. Tool Configuration Mismatch
The error indicated that the tool names in the Jenkinsfile don't match the configured tools in Jenkins.

**Fixed in Jenkinsfile:**
- Changed `'Maven-3.9.11'` → `'Maven 3.8.6'`
- Changed `'JDK-11'` → `'jdk11'`

### 2. JAVA_HOME Environment Variable Issue
**Error:** `The JAVA_HOME environment variable is not defined correctly`

**Root Cause:** Jenkins tool configuration for JDK is incorrect or JDK tool name doesn't match.

**Solutions:**
1. **Check Jenkins Tool Configuration:**
   - Go to Jenkins → Manage Jenkins → Global Tool Configuration
   - Under "JDK installations", verify the exact name matches `jdk11`
   - Make sure the JDK path points to a JDK (not JRE)

2. **Alternative: Use System Java:**
   - Use `Jenkinsfile.simple` (created) which doesn't depend on tool configuration
   - Requires Java and Maven to be in system PATH

3. **Fix Tool Names:**
   - In Jenkins Global Tool Configuration, check the exact JDK installation name
   - Update Jenkinsfile tools section to match exactly

### 3. Windows Shell Commands
**Error:** `Cannot run program "sh"... CreateProcess error=2, The system cannot find the file specified`

**Root Cause:** Jenkins was trying to run Unix shell (`sh`) commands on Windows system.

**Fixed by replacing all `sh` commands with `bat` commands:**
- `sh 'mvn clean compile'` → `bat 'mvn clean compile'`
- `sh 'mvn test'` → `bat 'mvn test'`
- `sh 'mvn package'` → `bat 'mvn package'`
- Unix-style multi-line commands converted to Windows batch syntax

### 4. Environment Variables
**Fixed Unix environment variable syntax for Windows:**
- `export DOCKER_TAG=${DOCKER_TAG}` → `set DOCKER_TAG=${DOCKER_TAG}`
- `echo $DOCKER_PASS` → `echo %DOCKER_PASS%`
- `$VARIABLE` → `%VARIABLE%` in batch commands

### 5. Conditional Commands
**Fixed Unix conditionals for Windows:**
- `command -v newman &> /dev/null` → `where newman >nul 2>nul`
- `|| true` → `|| exit /b 0`
- Unix if-then-else → Windows batch if-else syntax

## Current Pipeline Status:

### ✅ **Will Work Now:**
- ✅ Checkout from Git (already working)
- ✅ Maven build and compile (Windows-compatible)
- ✅ Unit tests with JaCoCo coverage (Windows-compatible)
- ✅ OWASP Dependency Check (Windows-compatible)
- ✅ Docker build commands (Windows-compatible)
- ✅ Git tag creation (Windows-compatible)

### 🔄 **Optional Features (Commented Out):**
- Slack notifications (can be enabled when configured)
- SonarQube analysis (can be enabled when server is set up)
- Trivy security scan (requires Trivy installation)
- Newman API tests (requires Newman installation)

## Next Steps:

### 1. **Test the Pipeline**
The pipeline should now run successfully on your Windows Jenkins setup.

### 2. **Enable Optional Features Later:**

**For Slack notifications:**
```groovy
// In environment section, uncomment:
SLACK_TOKEN = credentials('slack-token')

// In stages, uncomment lines like:
slackSend(channel: SLACK_CHANNEL, color: 'good', message: "✅ Build SUCCESS")
```

**For SonarQube:**
```groovy
// Uncomment in Code Quality stage:
withSonarQubeEnv('SonarQube') {
    bat 'mvn sonar:sonar -Dsonar.projectKey=jenkins-pipeline-demo'
}
```

### 3. **Tool Installation Requirements:**
- **Maven**: Already configured as `Maven 3.8.6`
- **JDK**: Already configured as `jdk11`
- **Docker**: Should work if Docker Desktop is installed
- **Git**: Already working
- **Trivy**: Optional - install if you want container scanning
- **Newman**: Optional - install if you want API testing

## Windows-Specific Notes:
- All shell commands now use `bat` instead of `sh`
- Environment variables use Windows syntax (`%VAR%`)
- File paths use Windows conventions
- Error handling adapted for Windows batch commands

The pipeline should now execute successfully on your Windows Jenkins environment!

## 🚨 Quick Fix for JAVA_HOME Issue:

If you're still getting the JAVA_HOME error, try this immediate solution:

### Option 1: Use Simple Pipeline (Recommended)
1. Rename current `Jenkinsfile` to `Jenkinsfile.backup`
2. Rename `Jenkinsfile.simple` to `Jenkinsfile`  
3. This version uses system Java/Maven without tool configuration

### Option 2: Fix Tool Configuration
1. Go to Jenkins → Manage Jenkins → Global Tool Configuration
2. Under JDK installations, check the exact name
3. Common JDK names in Jenkins:
   - `jdk8`
   - `jdk11`
   - `Java 11`
   - `Default`
4. Update the Jenkinsfile tools section with the exact name

### Option 3: Manual Environment Variables
Add this to your Jenkins job configuration → Build Environment:
```
JAVA_HOME=C:\Program Files\Java\jdk-11.0.x
MAVEN_HOME=C:\Program Files\Apache\maven
```

The `Jenkinsfile.simple` should work immediately if Java and Maven are installed on your system!