#!/bin/bash

cd "$(dirname "$0")" || exit 1

SRC_DIR="src"
BIN_DIR="bin"
OUTPUT_DIR="output"

case "$(uname -s)" in
  Linux*)
    PLATFORM="linux"
    ;;
  Darwin*)
    PLATFORM="mac"
    ;;
  *)
    echo "Unsupported platform: $(uname -s)"
    exit 1
    ;;
esac

LIB_DIR="lib/$PLATFORM"
LIB_JAR="$LIB_DIR/cpdfconversionsdk.jar"

if [ ! -f "$LIB_JAR" ]; then
  echo "Missing library jar: $LIB_JAR"
  exit 1
fi

mkdir -p "$BIN_DIR"
mkdir -p "$OUTPUT_DIR"

javac -cp "$LIB_JAR" -d "$BIN_DIR" "$SRC_DIR"/App.java

if [ $? -ne 0 ]; then
  echo "❌ Compile failed"
  exit 1
fi

java -Djava.library.path="$LIB_DIR" -cp "$BIN_DIR:$LIB_JAR" App "$(pwd)"