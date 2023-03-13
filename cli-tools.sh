#!/usr/bin/env bash

usage() {
  echo "Usage: ${0##*/} [ OPTIONS ]" 1>&2
  echo -e "\nKafka admin command line tool" 1>&2
  echo -e "\nOptions:" 1>&2
  echo -e "  -c,  --consume          cli-tools.sh -c <topic-name> (Consume data from kafka topic with key and value)" 1>&2
  echo -e "  -p,  --produce          cli-tools.sh -p <topic-name> (Produce : separated key value data to kafka topic)" 1>&2
  echo -e "  -l,  --all-topics       List all available Kafka topics" 1>&2
  echo -e "  -g,  --consumer-groups   Advanced Kafka command relies on kafka-consumer-groups.sh
                          e.g.
                          cli-tools.sh -g --list (List all consumer groups)
                          cli-tools.sh -g --describe --group room-event-aggregator (Describe consumer group and list offset lag related to given group)
                          cli-tools.sh -g --describe --all-groups (Describe all consumer-groups and list offset lag related to given groups)"  1>&2
  echo -e "  -h,  --help             Show this help" 1>&2
}

if [ -z "${BOOTSTRAP_SERVERS}" ]; then
  BOOTSTRAP_SERVERS="localhost:9093"
fi

if [ $# -eq 0 ]; then
    >&2 usage; exit 0
fi

DOCKER_EXEC="docker-compose exec kafka"
TOPIC_CMD="${DOCKER_EXEC} /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server ${BOOTSTRAP_SERVERS} --list"
CONSUME_CMD="${DOCKER_EXEC} /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server ${BOOTSTRAP_SERVERS} --from-beginning --property print.key=true --property key.separator=: --topic"
PRODUCE_CMD="${DOCKER_EXEC} /opt/bitnami/kafka/bin/kafka-console-producer.sh --bootstrap-server ${BOOTSTRAP_SERVERS} --property parse.key=true --property key.separator=: --topic"
CONSUME_GRP_CMD="${DOCKER_EXEC} /opt/bitnami/kafka/bin/kafka-consumer-groups.sh --bootstrap-server ${BOOTSTRAP_SERVERS}"

while true; do
  case "$1" in
    -c  | --consume ) eval "$CONSUME_CMD $2"; shift ;;
    -p  | --produce ) eval "$PRODUCE_CMD $2"; shift ;;
    -l  | --all-topics ) eval "$TOPIC_CMD $2"; shift ;;
    -g  | --consumer-groups ) eval "$CONSUME_GRP_CMD ${*:2}"; shift ;;
    -h  | --help ) usage; exit 0 ;;
    -- ) shift; break ;;
    * ) break ;;  esac
done
