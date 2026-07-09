#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f ".env" ]; then
    export $(grep -v '^#' .env | xargs)
fi

# Create directories if they don't exist
mkdir -p target/classes
mkdir -p lib

# Download MySQL connector if not present
if [ ! -f "lib/mysql-connector-j.jar" ]; then
    echo "Downloading MySQL Connector/J..."
    curl -L -o lib/mysql-connector-j.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
fi

# Compile all Java sources
echo "Compiling Java sources..."
find src/main/java -name "*.java" -print0 | xargs -0 javac -cp "lib/*" -d target/classes -sourcepath src/main/java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful!"

# Run based on argument
if [ "$1" == "server" ]; then
    echo "Starting VC Controller Server..."
    java -cp "target/classes:lib/*" com.vehicularcloud.controller.VCControllerServer
elif [ "$1" == "client" ]; then
    echo "Starting Vehicular Cloud Application..."
    java -cp "target/classes:lib/*" com.vehicularcloud.VehicularCloudApplication
else
    echo "Usage: ./run.sh [server|client]"
    echo "  server - Start the VC Controller Server (port 6000)"
    echo "  client - Start the main GUI application"
fi
