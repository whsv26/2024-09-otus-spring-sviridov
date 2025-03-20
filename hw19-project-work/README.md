## C,jhrf

```shell
../mvnw clean && \ 
../mvnw install -pl novel/novel-model,rating/rating-model 
../mvnw compile jib:build -pl gateway,user/user-api,novel/novel-api,novel/novel-outbox,search/search-indexer,search/search-api,rating/rating-api,rating/rating-consumer 
```