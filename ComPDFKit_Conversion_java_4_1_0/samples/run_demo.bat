@echo off
cd /d "%~dp0"
set SRC_DIR=src
set BIN_DIR=bin
set OUTPUT_DIR=output
set LIB_DIR=lib\windows
set LIB_JAR=%LIB_DIR%\cpdfconversionsdk.jar

if not exist "%LIB_JAR%" (
    echo Missing library jar: %LIB_JAR%
    exit /b 1
)

if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

echo Compiling...

javac -cp "%LIB_JAR%" -d "%BIN_DIR%" "%SRC_DIR%\App.java"
if %errorlevel% neq 0 (
    echo Compile failed
    exit /b 1
)

echo Running...

set "PATH=%cd%\%LIB_DIR%;%PATH%"
java -Djava.library.path="%LIB_DIR%" -cp "%BIN_DIR%;%LIB_JAR%" App "%cd%"