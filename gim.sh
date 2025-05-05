#!/bin/bash

if ! command -v docker &> /dev/null
then
    echo "Docker is not installed. Please install Docker and try again."
    exit 1
fi

if [ ! -f docker-compose.yml ]; then
    echo "docker-compose.yml file not found. Please ensure it exists in the current directory."
    exit 1
fi

if [ -f .env ]; then
    export $(cat .env | xargs)
else
    echo ".env file not found. Please ensure it exists in the current directory."
    exit 1
fi

docker-compose up -d

echo "Press any key to shut down..."
read -n 1 -s

docker-compose down -v
