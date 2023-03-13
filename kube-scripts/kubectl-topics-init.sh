#!/usr/bin/env bash

TOPICS_IMAGE="ghcr.io/versicherungskammer/coding-dojo-event-sourcing/topics-init:main"

kubectl delete pod topics-init 2> /dev/null
kubectl run topics-init \
            --env KAFKA_BOOTSTRAP_SERVERS=ddi-cluster-kafka-bootstrap.ddi-kafka:9092 \
            --image-pull-policy=Always \
            --restart=Never \
            --image $TOPICS_IMAGE
kubectl wait pod/topics-init --timeout=10s --for condition=Ready
kubectl logs -f topics-init
