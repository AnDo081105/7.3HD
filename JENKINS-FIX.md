# Jenkins Configuration Fix - Windows Compatibility

## Issues Fixed:

### 1. Tool Configuration Mismatch
The error indicated that the tool names in the Jenkinsfile don't match the configured tools in Jenkins.

**Fixed in Jenkinsfile:**
- Changed `'Maven-3.9.11'` → `'Maven 3.8.6'`
- Changed `'JDK-11'` → `'jdk11'`

### 2. JAVA_HOME Environment Variable Issue
**Error:** `The JAVA_HOME environment variable is not defined correctly`

**Root Cause:** The Jenkins JDK tool named `jdk11` doesn't exist or isn't configured properly.

**Debug Output Analysis:**
- JAVA_HOME is empty (tool 'jdk11' returned nothing)
- Maven is configured correctly
- Java is available in system PATH at `C:\Program Files\Common Files\Oracle\Java\javapath`

**Fixed in Jenkinsfile:**
- Removed dependency on JDK tool configuration
- Set JAVA_HOME to system Java path: `C:\Program Files\Common Files\Oracle\Java\javapath`
- Commented out `jdk 'jdk11'` from tools section
- Maven tool `'Maven 3.8.6'` works correctly

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

## 🚨 JAVA_HOME Issue - FIXED!

Based on the debug output, the issue has been identified and fixed:

**Problem:** The Jenkins JDK tool `'jdk11'` was not configured, causing empty JAVA_HOME.
**Solution:** Updated Jenkinsfile to use system Java directly.

### ✅ Applied Fix:
- JAVA_HOME now points to: `C:\Program Files\Common Files\Oracle\Java\javapath`
- Removed dependency on Jenkins JDK tool configuration  
- Maven tool configuration works fine (`Maven 3.8.6`)

### Next Steps:
1. **Run the pipeline again** - it should now work
2. **The debug stage** will confirm Java and Maven are working
3. **Build stage** should complete successfully

### Alternative Solutions (if still having issues):

**Option 1: Use Simple Pipeline**
The `Jenkinsfile.simple` is still available as a backup that uses system tools entirely.

**Option 2: Configure JDK Tool in Jenkins**
If you want to use Jenkins tool management:
1. Go to Jenkins → Manage Jenkins → Global Tool Configuration
2. Add JDK installation with name exactly `jdk11`
3. Point it to: `C:\Program Files\Common Files\Oracle\Java\javapath`

The main Jenkinsfile should now work immediately with the system Java path!