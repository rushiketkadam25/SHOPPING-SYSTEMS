@echo off
title AuraCommerce - Launcher
echo ========================================================
echo        Launching AuraCommerce Enterprise Suite
echo ========================================================
echo.

if not exist "bin\com\shopping\Main.class" (
    echo Binary not found in bin\. Running build first...
    call build.bat
)

echo Starting application...
java -cp "bin;lib\flatlaf-3.4.1.jar;lib\mysql-connector-j-8.3.0.jar;src\main\resources" com.shopping.Main
