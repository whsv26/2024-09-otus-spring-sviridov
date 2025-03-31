## Как запустить

```shell
cd hw19-project-work
```

### В Docker Compose

Собираем образы микросервисов и заливаем их в локальный Docker daemon.
```shell
../mvnw clean && ../mvnw install -pl libs/outbox,libs/auth,libs/idempotency,services/novel/novel-model,services/rating/rating-model 
../mvnw compile jib:dockerBuild -pl services/gateway/gateway-api,services/user/user-api,services/novel/novel-api,services/novel/novel-outbox,services/search/search-indexer,services/search/search-api,services/rating/rating-api,services/rating/rating-consumer 
```

Запускаем все сервисы
```shell
docker compose up
```

### В Kubernetes кластере (Minikube)

Запускаем Minikube
```shell
minikube start
```

Настраиваем Nginx Ingress для доступа к кластеру с хост-машины.
Без тонелля Ingress может быть недоступен с хост-машины.
```shell
minikube addons enable ingress
```

Для MacOS нужно запустить тоннель, без которого не получится достучаться к Ingress.
После запуска можно обращаться к кластеру через http://localhost:80
```shell
minikube tunnel
```

Для Linux, чтобы попасть в кластер, нужно обращаться к IP-адресу миникуба, который можно получить следующей командой
```shell
minikube ip
```

Для Linux можно переопределить baseUrl для окружения миникуба в HTTP-клиенте Intellij следующей командой
```shell
cp http-client.env.json http-client.private.env.json
jq --arg ip "$(minikube ip)" '.minikube.baseUrl = "http://\($ip):80"' http-client.env.json > http-client.private.env.json
```

Собираем образы микросервисов и заливаем их в Docker daemon миникуба (у него есть свой внутри).
```shell
eval $(minikube docker-env)
../mvnw clean && ../mvnw install -pl libs/outbox,libs/auth,libs/idempotency,services/novel/novel-model,services/rating/rating-model 
../mvnw compile jib:dockerBuild -pl services/gateway/gateway-api,services/user/user-api,services/novel/novel-api,services/novel/novel-outbox,services/search/search-indexer,services/search/search-api,services/rating/rating-api,services/rating/rating-consumer
```

Деплой приложения в Minikube (потребуется Helm и Helmfile)
```shell
helm plugin install https://github.com/databus23/helm-diff
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add incubator https://charts.helm.sh/incubator
helmfile apply -e local -f deploy/helmfile.yaml
```

Чтобы откатить деплой, можно все почистить командой
```
helmfile destroy -e local -f deploy/helmfile.yaml
```

Для графаны для удобства добавлен Ingress для хоста http://grafana.local.
Чтобы им воспользоваться, можно добавить новую запись в `/etc/hosts`.
```shell
echo "$(minikube ip)$(printf '\t')grafana.local" | sudo tee -a /etc/hosts
```

## Как обновить C4-диаграмму 

Запускаем Structurizr Lite 
```shell
cd hw19-project-work/structurizr
docker run -it --rm -p 8080:8080 -v ./:/usr/local/structurizr structurizr/lite
```

Теперь по http://localhost:8080 можно через UI открыть диаграммы, отредактировать и экспортировать их в SVG.

## Как обновить презентацию

Запускаем dev-сервер, который рендерит презентацию по пути http://localhost:3030
```shell
cd hw19-project-work/presentation
npm i -g pnpm
pnpm install
pnpm dev
```

Вносим изменения в [slides.md](presentation/slides.md)