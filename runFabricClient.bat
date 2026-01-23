@echo off
REM Quick launch script for Fabric client testing
REM Usage: runFabricClient.bat

echo ========================================
echo  Limited Spectator - Fabric Client
echo ========================================
echo.
echo Starting Fabric development client...
echo.

gradlew.bat :fabric:runClient

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Failed to start Fabric client
    echo Check the error messages above
    pause
    exit /b %ERRORLEVEL%
)
