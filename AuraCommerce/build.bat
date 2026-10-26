@echo off
title AuraCommerce - Build Script
echo ========================================================
echo        Building AuraCommerce Java Project
echo ========================================================
echo.

if not exist "bin" mkdir bin

echo Compiling Java source files...
javac -cp "lib\flatlaf-3.4.1.jar;lib\mysql-connector-j-8.3.0.jar" -d bin src\main\java\com\shopping\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Compilation completed without errors!
    echo Output directory: bin\
    echo Copying properties file to bin\...
    copy src\main\resources\db_config.properties bin\ >nul 2>&1
) else (
    echo.
    echo [ERROR] Compilation failed. Please check Java errors above.
)
pause
