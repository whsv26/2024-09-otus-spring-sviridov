## C,jhrf

```shell
cd hw19-project-work
../mvnw clean && \ 
../mvnw install -pl novel/novel-model,rating/rating-model,libs/outbox 
../mvnw compile jib:build -pl gateway,user/user-api,novel/novel-api,novel/novel-outbox,search/search-indexer,search/search-api,rating/rating-api,rating/rating-consumer 
```