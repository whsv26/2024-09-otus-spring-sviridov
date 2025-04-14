workspace {

    !identifiers hierarchical

    model {
        client = person "User" {
            description "End user of the system"
        }

        gateway = softwareSystem "API Gateway" {
            description "• Traffic routing\n• JWT validation\n• Resilience (Circuit Breaker, Rate Limiter, etc.)\n• Request tracing"
            gatewayApi = container "gateway-api" {
                description "• Traffic routing\n• JWT validation\n• Resilience (Circuit Breaker, Rate Limiter, etc.)\n• Request tracing"
                technology "Spring Cloud API Gateway, REST, API, Stateless, WebFlux"
            }
            gatewayRedis = container "gateway-redis" {
                description "Stores Rate Limiter state"
                technology "Redis"
                tags "Database"
            }

            gatewayApi -> gatewayRedis "Read/Write"
        }

        user = softwareSystem "User Management" {
            description "• User registration\n• JWT token issuance\n• Profile management"
            userApi = container "user-api" {
                description "• User registration\n• JWT token issuance\n• Profile management"
                technology "Spring Boot, REST, API, Stateless"
            }
            userPostgres = container "user-postgres" {
                description "Stores application users"
                technology "PostgreSQL"
                tags "Database"
            }
            userApi -> userPostgres "Read/Write users"
        }

        novel = softwareSystem "Web Novel Content Management" {
            description "• Uploading and editing works\n• Structuring by chapters"
            novelApi = container "novel-api" {
                description "• Uploading and editing works\n• Structuring by chapters"
                technology "Spring Boot, REST, API, Stateless"
            }
            novelMongodb = container "novel-mongodb" {
                description "Stores web novels and their events"
                technology "MongoDB"
                tags "Database"
            }
            novelOutbox = container "novel-outbox" {
                description "Publishes events about web novel state changes"
                technology "Spring Boot, CDC, Change Stream"
            }
            novelEvent = container "novel-event" {
                description "Events about creation, modification, or deletion of a web novel"
                technology "Kafka"
                tags "Topic"
            }

            novelApi -> novelMongodb "Read/Write web novels and their events"
            novelOutbox -> novelMongodb "Reads web novel events"
            novelOutbox -> novelEvent "Publishes web novel events"
        }

        rating = softwareSystem "Web Novel Rating" {
            description "Allows users to rate works"
            ratingApi = container "rating-api" {
                description "Allows users to rate works"
                technology "Spring Boot, REST, API, Stateless"
            }
            ratingCommand = container "rating-command" {
                description "Commands for adding a work rating"
                technology "Kafka"
                tags "Topic"
            }
            ratingConsumer = container "rating-consumer" {
                description "Transactionally and idempotently updates the current average rating of a web novel"
                technology "Spring Boot"
            }
            ratingEvent = container "rating-event" {
                description "Events about changes in the average rating of a work"
                technology "Kafka"
                tags "Topic"
            }
            ratingRedis = container "rating-redis" {
                description "• Stores rating counter and sum\n• Stores idempotency keys"
                technology "Redis"
                tags "Database"
            }
            ratingApi -> ratingCommand "Sends command to add rating to a work"
            ratingApi -> ratingRedis "Gets average rating using counter and sum"
            ratingConsumer -> ratingCommand "Reads command to add rating to a work"
            ratingConsumer -> ratingRedis "Transactionally and idempotently increases rating counter and sum"
            ratingConsumer -> ratingEvent "Publishes event about change in average rating"
        }

        search = softwareSystem "Web Novel Search" {
            description "Search for web novels using various filters: author, rating, title, etc."
            searchApi = container "search-api" {
                description "Search for web novels using various filters: author, rating, title, etc."
                technology "Spring Boot, REST, API, Stateless"
            }
            searchElasticsearch = container "search-elasticsearch" {
                description "Stores web novels in a format suitable for search"
                technology "ElasticSearch"
                tags "Database"
            }
            searchIndexer = container "search-indexer" {
                description "Indexes web novels"
                technology "Spring Boot"
            }

            searchIndexer -> novel.novelEvent "Reads events about web novel changes"
            searchIndexer -> rating.ratingEvent "Reads events about web novel rating changes"
            searchIndexer -> user.userApi "Retrieves author profile"
            searchIndexer -> searchElasticsearch "Writes web novels to index"
            searchApi -> searchElasticsearch "Performs web novel search by criteria"
        }

        client -> gateway.gatewayApi
        gateway.gatewayApi -> user.userApi "Forwards requests; Retrieves public key for JWT validation"
        gateway.gatewayApi -> novel.novelApi "Forwards requests"
        gateway.gatewayApi -> search.searchApi "Forwards requests"
        gateway.gatewayApi -> rating.ratingApi "Forwards requests"
    }

    views {
        systemLandscape landscape {
            include *
            exclude "search -> *"
        }

        container gateway gateway {
            include *
            exclude "search -> *"
        }

        container user user {
            include *
            exclude gateway
        }

        container novel novel {
            include *
            exclude gateway
        }

        container search search {
            include *
            exclude gateway
        }

        container rating rating {
            include *
            exclude gateway
        }

        styles {
            element "Database" {
                shape cylinder
            }

            element "Topic" {
                shape pipe
            }
        }

        theme default
    }
}
