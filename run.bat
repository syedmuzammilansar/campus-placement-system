@echo off
title Campus Placement System
echo ======================================================================
echo    Campus Placement Eligibility & Skill-Ranking System
echo ======================================================================
echo.

where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [*] Found Apache Maven! Building and executing project...
    call mvn clean compile exec:java
    pause
    exit /b
)

echo [*] Maven not found. Compiling with javac...
if not exist "bin" mkdir bin
if not exist "database" mkdir database
if not exist "exports" mkdir exports

dir /s /b src\main\java\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

echo [*] Running Application...
java -cp "bin;lib/*" com.placement.app.Main
pause
