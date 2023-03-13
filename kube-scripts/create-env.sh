#!/usr/bin/env bash

cd $(dirname "$0")

if [ -f ../.env ]; then
  printf "The configuration file '../.env' already exists.\n"
else
  printf "Generate '../.env' for overriding default configuration values\n"
  cat <<EOF > "../.env"
# provide the hostname where your Traefik in Kubernetes can be reached
K8S_SERVER=

# provide the hostname where your Kafka can be reached
KAFKA_BOOTSTRAP_SERVERS=
EOF

printf "Please edit the file '../.env' and run the script again.\n"
fi
