#!/usr/bin/env bash
echo "======================================================================"
echo "   Campus Placement Eligibility & Skill-Ranking System"
echo "======================================================================"
echo ""

if command -v mvn &> /dev/null; then
    echo "[*] Found Apache Maven! Building and running project..."
    mvn clean compile exec:java
    exit 0
fi

echo "[*] Maven not found. Compiling with javac..."
mkdir -p bin database exports
find src/main/java -name "*.java" > sources.txt
javac -d bin @sources.txt
rm sources.txt

echo "[*] Running Application..."
java -cp "bin:lib/*" com.placement.app.Main
