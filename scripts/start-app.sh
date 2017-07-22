#!/usr/bin/env bash

echo $PATH
./scripts/start-mongodb.sh
./scripts/build-docker-image.sh

