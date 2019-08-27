#!/bin/bash

if [ -z ${LOCAL_DEV} ]
then
  echo '#################################################################'
  echo "Configuring ${APP_ENV} Environment"
  # Add Deployment Configuration here outside local development
fi

echo '#################################################################'
echo 'Starting Application'
echo '#################################################################'
export SPRING_DATASOURCE_URL="jdbc:postgresql://${RDS_DB_HOST}:${RDS_DB_PORT}/${RDS_DB_NAME}"
export SPRING_DATASOURCE_USERNAME="${RDS_DB_USER}"
export SPRING_DATASOURCE_PASSWORD="${RDS_DB_PASSWORD}"
java -jar ROOT.jar
