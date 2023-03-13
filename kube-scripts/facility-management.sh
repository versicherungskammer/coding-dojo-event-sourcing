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
  if [ -z "$K8S_SERVER" ]; then
    echo "K8S_SERVER is not set in ../.env"
    exit 1
  fi
else
  ./create-env.sh
  exit 1
fi

mkdir -p ../build
envsubst '$KAFKA_BOOTSTRAP_SERVERS,$K8S_SERVER' < mock-facility-management.yaml > ../build/k8s.yaml
kubectl apply -f ../build/k8s.yaml
