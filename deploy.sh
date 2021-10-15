mvn clean install -DskipTests
docker build . --tag gibdd:latest
docker-compose -f docker-compose.yml up -d