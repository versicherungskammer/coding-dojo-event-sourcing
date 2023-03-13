#!/usr/bin/env bash

if type jq > /dev/null 2>&1 ; then
  exit 0
fi

echo "Please install 'jq'. See https://stedolan.github.io/jq/download/"
exit 1
