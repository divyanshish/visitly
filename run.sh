#!/bin/bash

echo "================================"
echo "  MYPROJECT - Single Run Setup"
echo "================================"

# Load environment variables from .env
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
    echo "✅ Loaded .env configuration"
else
    echo "⚠️  .env file not found. Using defaults."
fi

# Check if MySQL is running
echo "Checking MySQL..."
if ! mysql -u $DATABASE_USERNAME -p$DATABASE_PASSWORD -e "SELECT 1;" 2>/dev/null; then
    echo "❌ Cannot connect to MySQL"
    echo "Please ensure MySQL is running with:"
    echo "   brew services start mysql"
    exit 1
fi

# Create database if not exists
echo "Setting up database..."
mysql -u $DATABASE_USERNAME -p$DATABASE_PASSWORD -e "CREATE DATABASE IF NOT EXISTS myproject_db;"

# Clean and build
echo "Building application..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Run application
echo "Starting application..."
echo "================================"
echo "✅ API: http://localhost:8080/api"
echo "✅ Swagger: http://localhost:8080/api/swagger-ui.html"
echo "✅ Health: http://localhost:8080/api/actuator/health"
echo "================================"

java -jar target/myproject-0.0.1-SNAPSHOT.jar
EOF

# Make it executable
chmod +x run.sh

# Verify it's executable
ls -la run.sh
# Should show: -rwxr-xr-x (with x = executable)
