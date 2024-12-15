@echo off
echo Starting Goal Status Update Test...

:: 設置 JAVA_HOME 和 Maven 路徑
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
set M2_HOME=C:\Program Files\Apache\maven
set PATH=%M2_HOME%\bin;%PATH%

:: 清理並編譯專案
echo Building project...
call mvn clean compile

:: 執行特定的測試類
echo Running Goal Status Update Test...
call mvn test -Dtest=GoalStatusUpdateTest

:: 檢查測試結果
if errorlevel 1 (
    echo Test failed!
    echo Check the test report for details.
) else (
    echo Test completed successfully!
)

:: 暫停以查看結果
pause