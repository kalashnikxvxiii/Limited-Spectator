@echo off
REM Quick launch script for Fabric server testing
REM Usage: runFabricServer.bat

echo ========================================
echo  Limited Spectator - Fabric Server
echo ========================================
echo.
echo Starting Fabric development server...
echo.

gradlew.bat :fabric:runServer

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Failed to start Fabric server
    echo Check the error messages above
    pause
    exit /b %ERRORLEVEL%
)
