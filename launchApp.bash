#!/usr/bin/env bash
POSTGRES="postgres-covid"
ZOOKEEPER="zookeeper"
KAFKA="kafka"
CONNECT="connect"

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
      exit 0
    fi
    printf .
    sleep 5
  done
  lf=$'\n'
  printf "$lf"
}

#Create postgres, zookeeper, kafka & connect containers
echo "### Launching PostgreSQL, Zookeeper, Kafka & Connect containers ###"
docker-compose -f docker-compose.yml up -d
waitContainer ${POSTGRES}
echo "### POSTGRES IS RUNNING ###"
waitContainer ${ZOOKEEPER}
echo "### ZOOKEEPER IS RUNNING ###"
waitContainer ${KAFKA}
echo "### KAFKA IS RUNNING ###"
waitContainer ${CONNECT}
echo "### KAFKA CONNECT IS RUNNING ###"

echo "### Creating Contamination Connector ###"
#POST Capture Data Change of contamination table and insert changes to contamination topic in kafka
curl -X POST -H "Accept:application/json" -H "Content-Type: application/json" --data @src/main/resources/contamination-connector.json http://localhost:8083/connectors

#POST Capture Data Change of dangerous_location topic and insert changes to location table in postgres
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @src/main/resources/dangerous-location-connector.json

echo "### Done ###"