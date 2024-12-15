@echo off
echo Starting Health Management System...

REM Get the directory where the batch file is located
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM Check if pom.xml exists
if not exist "pom.xml" (
    echo Error: pom.xml not found in the current directory.
    echo Please make sure you're running this script from the project root directory.
    pause
    exit /b 1
)

REM Check if Docker is running
docker info > nul 2>&1
if %errorlevel% neq 0 (
    echo Docker is not running. Please start Docker Desktop first.
    pause
    exit /b 1
)

REM Stop and remove existing containers (if any)
echo Cleaning up existing containers...
docker-compose down

REM Start the database container
echo Starting database container...
docker-compose up -d

REM Wait for database to be ready
echo Waiting for database to be ready...
timeout /t 3 /nobreak

REM Build and run the Java application
echo Building and running the application...
call mvn clean package
if %errorlevel% neq 0 (
    echo Maven build failed
    pause
    exit /b 1
)

REM Run the application
echo Starting the application...
java -jar target/health-management-system-1.0-SNAPSHOT-jar-with-dependencies.jar

REM Keep the window open if there's an error
if %errorlevel% neq 0 (
    echo Application exited with error code %errorlevel%
    pause
)
