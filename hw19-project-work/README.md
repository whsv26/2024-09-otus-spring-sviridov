## C,jhrf

```shell
cd hw19-project-work
../mvnw clean && ../mvnw install -pl libs/outbox,libs/auth,libs/idempotency,services/novel/novel-model,services/rating/rating-model 
../mvnw compile jib:build -pl services/gateway/gateway-api,services/user/user-api,services/novel/novel-api,services/novel/novel-outbox,services/search/search-indexer,services/search/search-api,services/rating/rating-api,services/rating/rating-consumer 
```

```shell
{service_name="user-api"} | json | line_format `{{.message}}`
```