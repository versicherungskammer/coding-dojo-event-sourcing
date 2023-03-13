#!/usr/bin/env bash

cd $(dirname "$0")

if [ -f ../.env ]; then
  set -a
  . ../.env
  set +a
  if [ -z "$KAFKA_BOOTSTRAP_SERVERS" ]; then
    echo "KAFKA_BOOTSTRAP_SERVERS is not set in ../.env"
    exit 1
  fi
else
  ./create-env.sh
  exit 1
fi

TOPICS_IMAGE="ghcr.io/versicherungskammer/coding-dojo-event-sourcing/topics-init:main"
kubectl delete pod topics-init 2> /dev/null
kubectl run topics-init \
            --env KAFKA_BOOTSTRAP_SERVERS=$KAFKA_BOOTSTRAP_SERVERS \
            --image-pull-policy=Always \
            --restart=Never \
            --image $TOPICS_IMAGE
kubectl wait pod/topics-init --timeout=10s --for condition=Ready
kubectl logs -f topics-init
