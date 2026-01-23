@echo off
REM Quick launch script for Quilt server testing
REM Usage: runQuiltServer.bat

echo ========================================
echo  Limited Spectator - Quilt Server
echo ========================================
echo.
echo Starting Quilt development server...
echo.

gradlew.bat :quilt:runServer

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Failed to start Quilt server
    echo Check the error messages above
    pause
    exit /b %ERRORLEVEL%
)
