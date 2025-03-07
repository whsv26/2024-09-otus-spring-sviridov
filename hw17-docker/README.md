Обернуть приложение в docker-контейнер

## Цель

Деплоить приложение в современном DevOps-стеке

## Результат

Обёртка приложения в Docker

## Описание

Задание выполняется на основе любого сделанного Web-приложения

- Обернуть приложение в docker-контейнер. 
  - Dockerfile принято располагать в корне репозитория. 
  - В image должна попадать JAR-приложения. 
  - Сборка в контейнере рекомендуется, но не обязательна.
- БД в собственный контейнер оборачивать не нужно (если только Вы не используете кастомные плагины)
- Настроить связь между контейнерами, с помощью docker-compose
- Опционально: сделать это в локальном кубе.
- Приложение желательно реализовать с помощью всех Best Practices Docker (логгирование в stdout и т.д.)

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