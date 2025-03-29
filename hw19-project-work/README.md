```
{service_name="user-api"} | json | line_format `{{.message}}`
```
```shell
echo "$(minikube ip)$(printf '\t')grafana.local" | sudo tee -a /etc/hosts
echo "$(minikube ip)$(printf '\t')jaeger.local" | sudo tee -a /etc/hosts
```

http://grafana.local

## Установка

### В Docker Compose

Для этого варианта установки подготовлен Dockerfile приложения

```shell
../mvnw clean && ../mvnw install -pl libs/outbox,libs/auth,libs/idempotency,services/novel/novel-model,services/rating/rating-model 
../mvnw compile jib:dockerBuild -pl services/gateway/gateway-api,services/user/user-api,services/novel/novel-api,services/novel/novel-outbox,services/search/search-indexer,services/search/search-api,services/rating/rating-api,services/rating/rating-consumer 
```

```shell
docker compose up
```

### В Kubernetes кластере (Minikube)

```shell
cd hw19-project-work
```

Запуск Minikube
```shell
minikube start
```

Настройка Nginx Ingress.
Без тонелля Ingress может быть недоступен с хост-машины.
```shell
minikube addons enable ingress
```

Для MacOS
```shell
minikube tunnel
```

Для Linux
```shell
minikube ip
```

```shell
cp http-client.env.json http-client.private.env.json
jq --arg ip "$(minikube ip)" '.minikube.baseUrl = "http://\($ip):80"' http-client.env.json > http-client.private.env.json
```

Этот вариант не использует заранее подготовленный Dockerfile и собирает образ внутри Minikube с помощью плагина Jib.
```shell
eval $(minikube docker-env)
../mvnw clean && ../mvnw install -pl libs/outbox,libs/auth,libs/idempotency,services/novel/novel-model,services/rating/rating-model 
../mvnw compile jib:dockerBuild -pl services/gateway/gateway-api,services/user/user-api,services/novel/novel-api,services/novel/novel-outbox,services/search/search-indexer,services/search/search-api,services/rating/rating-api,services/rating/rating-consumer
```

Деплой приложения в Minikube (потребуется Helm)
```shell
helm plugin install https://github.com/databus23/helm-diff
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add incubator https://charts.helm.sh/incubator
helmfile apply -e local -f deploy/helmfile.yaml
helmfile destroy -e local -f deploy/helmfile.yaml
```