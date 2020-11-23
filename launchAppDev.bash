#!/usr/bin/env bash
POSTGRES="postgres-covid"
ZOOKEEPER="zookeeper"
KAFKA="kafka"
CONNECT="connect"
JDBCSINK_URL="https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.0.0/confluentinc-kafka-connect-jdbc-10.0.0.zip"

# Get the status of the health of the container name passed in parameter.
# Health = "starting" || Health = "healthy" || Health = "unhealthy"
function getContainerHealth {
  docker inspect --format "{{json .State.Health.Status }}" $1
}

# Wait for a container to be healthy or unhealthy.
# It prints a "." during starting and sleep 5 seconds before checking the status of the container.
function waitContainer {
  while STATUS=$(getContainerHealth $1); [[ $STATUS != "\"healthy\"" ]]; do
    if [[ $STATUS == "\"unhealthy\"" ]]; then
      echo "Failed!"
      exit
    fi
    printf .
    sleep 5
  done
  lf=$'\n'
  printf "%s" "$lf"
}

#Create postgres, zookeeper, kafka & connect containers
echo "### Launching PostgreSQL, Zookeeper, Kafka & Connect containers ###"
docker-compose -f docker-compose.yml up -d
waitContainer ${POSTGRES}
echo "### POSTGRES IS RUNNING ###"
echo "### Fetching JDBCSink ###"
wget ${JDBCSINK_URL}
echo "### Extracting Archive ###"
unzip confluentinc-kafka-connect-jdbc-10.0.0.zip
echo "### Initializing JdbcSink plugin for kafka connect ###"
docker cp confluentinc-kafka-connect-jdbc-10.0.0 connect:/kafka/connect
rm -rf confluentinc-kafka-connect-jdbc-10.0.0.zip
rm -rf confluentinc-kafka-connect-jdbc-10.0.0
echo "### Restarting Kafka connect to apply changes ###"
docker restart ${CONNECT}

#Waiting for Kafka connect to be available
while HTTP_STATUS=$(curl -o -/dev/null -s -w "%{http_code}\n" http://localhost:8083); [[ $HTTP_STATUS != "200" ]]; do
  printf .
  sleep 5
done

echo "### Creating Contamination Connector ###"
#POST Capture Data Change of contamination table and insert changes to contamination topic in kafka
curl -X POST -H "Accept:application/json" -H "Content-Type: application/json" --data @src/main/resources/contamination-connector.json http://localhost:8083/connectors

#POST Capture Data Change of dangerous_location topic and insert changes to location table in postgres
curl -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @src/main/resources/dangerous-location-connector.json

echo "### Done ###"