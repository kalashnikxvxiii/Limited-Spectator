@echo off
REM Quick launch script for Quilt client testing
REM Usage: runQuiltClient.bat

echo ========================================
echo  Limited Spectator - Quilt Client
echo ========================================
echo.
echo Starting Quilt development client...
echo.

gradlew.bat :quilt:runClient

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Failed to start Quilt client
    echo Check the error messages above
    pause
    exit /b %ERRORLEVEL%
)
