#!/usr/bin/env bash

cd $(dirname "$0")

./install-jq.sh

if [ -f ../.env ]; then
  set -a
  . ../.env
  set +a
  if [ -z "$K8S_SERVER" ]; then
    echo "K8S_SERVER is not set in ../.env"
    exit 1
  fi
else
  ./create-env.sh
  exit 1
fi

getFeedback() {
  FEEDBACK_PATH=$1
  echo -e "\nWaiting for feedback"
  # loop 10 times
  for i in {1..10}; do
    # check feedback
    STATUS_CODE=$(curl -s -o response.json -w "%{http_code}" -X GET -H "Content-Type: application/json" -d '{"name":"'${ROOM}'"}' ${FACILITY_URL}${FEEDBACK_PATH})
    TYPE=$(jq '.type' response.json)
    MESSAGE=$(jq '.message' response.json)
    echo STATUS_CODE=$STATUS_CODE / TYPE=$TYPE / MESSAGE=$MESSAGE
    if [ "$STATUS_CODE" == "303" ]; then
      echo -e "\nRoom '${ROOM}' created with id $(jq '.reference.id' response.json)"
      break
    fi
    # wait 1 second
    sleep 1
  done
}

FACILITY_URL="http://facility.${K8S_SERVER}"
# test if parameter 1 is list, create, update, delete, lock or unlock
if [ "$1" != "list" ] && [ "$1" != "create" ] && [ "$1" != "update" ] && [ "$1" != "delete" ] && [ "$1" != "lock" ] && [ "$1" != "unlock" ]; then
  echo "Usage: $0 list|create|update|delete|lock|unlock"
  exit 1
fi

if [ "$1" == "list" ]; then
  echo "List of rooms"
  STATUS_CODE=$(curl -s -o response.json -w "%{http_code}" ${FACILITY_URL}/rooms)
  echo STATUS_CODE=$STATUS_CODE
  ( echo "Name|Maintenance|Id"; \
    jq -r '.[] | .name + "|" + (.maintenance | tostring) + "|" + .id' response.json | sort
  ) | column -s '|' -t
elif [ "$1" == "create" ]; then
  shift 1
  ROOM=$1
  if [ -z "$ROOM" ]; then
    echo "Usage: $0 create <room>"
    exit 1
  fi

  echo "Create room '${ROOM}'"
  STATUS_CODE=$(curl -s -o response.json -w "%{http_code}" -X POST -H "Content-Type: application/json" -d '{"name":"'${ROOM}'"}' ${FACILITY_URL}/rooms)
  FEEDBACK_PATH=$(echo $RESPONSE | jq -r '.path' response.json)
  echo STATUS_CODE=$STATUS_CODE / FEEDBACK_PATH=$FEEDBACK_PATH
  if [ "$STATUS_CODE" != "202" ]; then
    echo "Room '${ROOM}' could not be created"
  fi
  getFeedback $FEEDBACK_PATH
elif [ "$1" == "delete" ]; then
  shift 1
  ROOM=$1
  ROOM_ID=$2
  if [ -z "$ROOM" -o -z "$ROOM_ID" ]; then
    echo "Usage: $0 delete <room> <room-id"
    exit 1
  fi

  echo "Delete room ${ROOM} with id '${ROOM_ID}'"
  STATUS_CODE=$(curl -s -o response.json -w "%{http_code}" -X DELETE -H "Content-Type: application/json" -d '{"name":"'${ROOM}'"}' ${FACILITY_URL}/rooms/${ROOM_ID})
  FEEDBACK_PATH=$(echo $RESPONSE | jq -r '.path' response.json)
  echo STATUS_CODE=$STATUS_CODE / FEEDBACK_PATH=$FEEDBACK_PATH
  if [ "$STATUS_CODE" != "202" ]; then
    echo "Room '${ROOM}' could not be deleted"
  fi
  getFeedback $FEEDBACK_PATH
fi

exit 0

#update room
curl -X PUT -H "Content-Type: application/json" -d '{"oldName":"test", "newName": "foobar"}' http://localhost:8080/rooms 
# 202 ACCEPTED 
# Location: /feedback/027c20f6-7bce-44b4-ae9c-21327930e29c 
# {"type":"unknown","id":"027c20f6-7bce-44b4-ae9c-21327930e29c","path":"/feedback/027c20f6-7bce-44b4-ae9c-21327930e29c"}% 
#... raum anzeigen und loeschen analog 
# feedback nachfragen 
curl http://localhost:8080/feedback/ed509646-c5dd-4d62-9888-1419e0e57991 
# 425 TOOEARLY (mit Location auf sich selbst) oder 303 SEE_OTHER (mit Locatuion zum Room) 
# Location: /rooms/7cdfa226-9c1c-4c3f-a7f8-584347d7c40b {"type":"success","reference":{"type":"room","id":"7cdfa226-9c1c-4c3f-a7f8-584347d7c40b","path":"/rooms/7cdfa226-9c1c-4c3f-a7f8-584347d7c40b"},"success":true 

# room sperren
curl -X POST -H "Content-Type: application/json" -d '{"name":"test"}' http://localhost:8080/rooms/7cdfa226-9c1c-4c3f-a7f8-584347d7c40b/lock 
# 202 ACCEPTED 
# Location: /feedback/027c20f6-7bce-44b4-ae9c-21327930e29c 
# {"type":"feedback","id":"027c20f6-7bce-44b4-ae9c-21327930e29c","path":"/feedback/027c20f6-7bce-44b4-ae9c-21327930e29c"}% 

# room sperre aufheben
curl -X DELETE -H "Content-Type: application/json" -d '{"name":"test"}' http://localhost:8080/rooms/7cdfa226-9c1c-4c3f-a7f8-584347d7c40b/lock 
# 202 ACCEPTED 
# Location: /feedback/027c20f6-7bce-44b4-ae9c-21327930e29c 
# {"type":"feedback","id":"027c20f6-7bce-44b4-ae9c-21327930e29c","path":"/feedback/027c20f6-7bce-44b4-ae9c-21327930e29c"}%
