#!/usr/bin/env bash

cd $(dirname $0)
TAG=$(git rev-parse HEAD)
TOPICS_IMAGE="ghcr.io/versicherungskammer/coding-dojo-event-sourcing/topics-init:$TAG"

kubectl delete pod topics-init 2> /dev/null
kubectl run topics-init \
            --env KAFKA_BOOTSTRAP_SERVERS=ddi-cluster-kafka-bootstrap.ddi-kafka:9092 \
            --restart=Never \
            --image $TOPICS_IMAGE
sleep 3
kubectl logs -f topics-init
