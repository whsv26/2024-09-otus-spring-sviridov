Wrap the Application in a Docker Container

## Goal

Deploy the application using a modern DevOps stack

## Result

Application wrapped in Docker

## Description

The task is based on any existing web application

- Wrap the application in a Docker container.
  - The Dockerfile is conventionally placed at the root of the repository.
  - The image should contain the JAR application.
  - Building the application inside the container is recommended but optional.
- No need to wrap the database in its own container (unless you're using custom plugins)
- Set up networking between containers using docker-compose
- Optional: do this in a local Kubernetes cluster (Minikube)
- Ideally, the application should follow all Docker best practices (logging to stdout, etc.)

## Setup

### In a Kubernetes Cluster (Minikube)

Start Minikube
```shell
minikube start
```

Configure Nginx Ingress.
Without a tunnel, Ingress might be inaccessible from the host machine.
```shell
minikube addons enable ingress
minikube tunnel
```

This option does not use a prebuilt Dockerfile and builds the image inside Minikube using the Jib plugin.
```shell
eval $(minikube docker-env)
../mvnw compile jib:dockerBuild
```

Deploy the application to Minikube (Helm is required)
```shell
helmfile apply -e local -f deploy/helmfile.yaml
```

Start the frontend
```shell
npm run-script dev
```

### In Docker Compose

A Dockerfile for the application is prepared for this installation option

```shell
docker compose up
```

Start the frontend
```shell
npm run-script dev
```