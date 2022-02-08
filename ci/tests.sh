#!/usr/bin/env bash

cd todoapi
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up --force-recreate --build todo_unit_tests
cd -
