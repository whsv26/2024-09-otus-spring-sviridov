Обернуть внешние вызовы в Hystrix

## Цель

Сделать внешние вызовы приложения устойчивыми к ошибкам

## Результат

Приложение с изолированными с помощью Hystrix внешними вызовами

## Описание

- Обернуть все внешние вызовы в Hystrix, Hystrix Javanica.
- Возможно использование Resilent4j
- Возможно использование Feign Client

Опционально: Поднять Turbine Dashboard для мониторинга.

## Установка

### В Kubernetes кластере (Minikube)

Запуск Minikube 
```shell
minikube start
```

Настройка Nginx Ingress. 
Без тонелля Ingress может быть недоступен с хост-машины.
```shell
minikube addons enable ingress
minikube tunnel
```

Этот вариант не использует заранее подготовленный Dockerfile и собирает образ внутри Minikube с помощью плагина Jib.
```shell
eval $(minikube docker-env)
../mvnw compile jib:dockerBuild
```

Деплой приложения в Minikube (потребуется Helm)
```shell
helmfile apply -e local -f deploy/helmfile.yaml
```

Старт фронта
```shell
npm run-script dev
```

### В Docker Compose

Для этого варианта установки подготовлен Dockerfile приложения 

```shell
docker compose up
```

Старт фронта
```shell
npm run-script dev
```