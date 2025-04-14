Wrap External Calls in Hystrix

## Goal

Make external calls of the application resilient to failures

## Result

Application with external calls isolated using Hystrix

## Description

- Wrap all external calls using Hystrix, Hystrix Javanica.
- Using Resilient4j is also acceptable
- Using Feign Client is also acceptable

Optional: Set up Turbine Dashboard for monitoring.

## Setup

### In Kubernetes Cluster (Minikube)

Start Minikube
```shell
minikube start
```

Configure Nginx Ingress.
Without the tunnel, Ingress might be inaccessible from the host machine.
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

Start frontend
```shell
npm run-script dev
```

### In Docker Compose

A Dockerfile is prepared for this installation option

```shell
docker compose up
```

Start frontend
```shell
npm run-script dev
```