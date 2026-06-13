#!/bin/sh

read -p "Base version (e.g. v26.06.0): " BASE_VERSION
read -p "Tags to apply (space separated): " TAGS

REGISTRY="git.jaypi4c.de/jaypi4c/tinia"

build_and_push() {
    SERVICE=$1
    DOCKERFILE=$2

    IMAGE="${REGISTRY}/${SERVICE}"

    echo "Building ${IMAGE}:${BASE_VERSION}"
    docker build -f "$DOCKERFILE" -t "${IMAGE}:${BASE_VERSION}" .

    echo "Pushing ${IMAGE}:${BASE_VERSION}"
    docker push "${IMAGE}:${BASE_VERSION}"

    for TAG in $TAGS; do
        if [ "$TAG" != "$BASE_VERSION" ]; then
            echo "Tagging ${IMAGE}:${TAG}"
            docker tag "${IMAGE}:${BASE_VERSION}" "${IMAGE}:${TAG}"
        fi

        echo "Pushing ${IMAGE}:${TAG}"
        docker push "${IMAGE}:${TAG}"
    done
}

build_and_push backend backend/Dockerfile
build_and_push frontend frontend/Dockerfile
build_and_push detector detector/Dockerfile
build_and_push extractor extractor/Dockerfile
build_and_push openehr openehr/Dockerfile