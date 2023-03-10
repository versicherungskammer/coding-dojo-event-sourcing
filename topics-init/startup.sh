#!/usr/bin/env bash

echo "Start Kafka Topics Init"

java -jar "/kafka-topics-init.jar" --config-file "$TOPICS_FILE" --create --read
