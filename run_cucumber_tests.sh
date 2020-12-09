#!/bin/bash
cd docker
docker-compose down
docker-compose pull
docker-compose up -d
printf  'Waiting for API'
until $(curl --output /dev/null --silent --head --fail http://localhost:8080/swagger-ui/); do
    printf '.'
    sleep 5
done
cd ../gamification-specs
mvn clean test
read -p "Press [ENTER] to terminate..."
