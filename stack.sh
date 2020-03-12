#!/usr/bin/env bash

set -e

command -v docker >/dev/null 2>&1 || { echo >&2 "docker is required but it's not installed. Aborting."; exit 1; }
command -v docker-compose >/dev/null 2>&1 || { echo >&2 "docker-compose is required but it's not installed. Aborting."; exit 1; }
command -v gcloud >/dev/null 2>&1 || { echo >&2 "gcloud is required but it's not installed. Aborting."; exit 1; }

function usage {
    echo "Usage: $0 [dev|up|down] [-h]"
    echo "  -h    Help. Display this message and quit."
    echo "  up    Stand up all outbound containers"
    echo "  down  Tear down all outbound containers."
    exit 0
}

function build {
  ./gradlew build
}

function configure_docker {
    gcloud auth configure-docker
}

function prepare_stack {
     # Ensure flexe-dev network exists
    if [[ $(docker network ls --filter name=flexe-dev --quiet) ]]; then
        echo "flexe-dev docker network already exists"
    else
        docker network create flexe-dev
    fi

    build
    configure_docker
    # ensure we have the event-store image
    docker pull eventstore/eventstore:release-5.0.6
}

function stack_up {
    prepare_stack
    docker-compose build
    docker-compose up -d
}

function stack_down {
    docker-compose down
}

while (( $# > 0 ))
do
    opt="$1"
    shift

    case $opt in
    up)
        stack_up
        exit 0
        ;;
    down)
        stack_down
        exit 0
        ;;
    -h)
        usage
        exit 0
        ;;
    --*)
        echo "Invalid option: '$opt'" >&2
        usage
        exit 1
        ;;
    *)
        # end of long options
        echo "Invalid command: '$opt'" >&2
        usage
        break;
        ;;
   esac
done
