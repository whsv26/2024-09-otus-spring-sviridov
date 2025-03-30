workspace {

    !identifiers hierarchical

    model {
        client = person "Пользователь" {
            description "Конечный пользователь системы"
        }

        gateway = softwareSystem "Шлюз API" {
            description "• Маршрутизация трафика\n• Проверка JWT\n• Resilience (Circuit Breaker, Rate Limiter, e.t.c.)\n• Трассировка запросов"
            gatewayApi = container "gateway-api" {
                description "• Маршрутизация трафика\n• Проверка JWT\n• Resilience (Circuit Breaker, Rate Limiter, e.t.c.)\n• Трассировка запросов"
                technology "Spring Cloud API Gateway, REST, API, Stateless, WebFlux"
            }
            gatewayRedis = container "gateway-redis" {
                description "Хранит состояние Rate Limiter"
                technology "Redis"
                tags "Database"
            }

            gatewayApi -> gatewayRedis "Чтение/Запись"
        }

        user = softwareSystem "Управление пользователями" {
            description "• Регистрация пользователей\n• Выдача JWT токенов\n• Управление профилями"
            userApi = container "user-api" {
                description "• Регистрация пользователей\n• Выдача JWT токенов\n• Управление профилями"
                technology "Spring Boot, REST, API, Stateless"
            }
            userPostgres = container "user-postgres" {
                description "Хранит пользователей приложения"
                technology "PostgreSQL"
                tags "Database"
            }
            userApi -> userPostgres "Чтение/Запись пользователей"
        }

        novel = softwareSystem "Управление контентом веб-новелл" {
            description "• Загрузка и редактирование произведений\n• Структурирование по главам"
            novelApi = container "novel-api" {
                description "• Загрузка и редактирование произведений\n• Структурирование по главам"
                technology "Spring Boot, REST, API, Stateless"
            }
            novelMongodb = container "novel-mongodb" {
                description "Хранит веб-новеллы и их события"
                technology "MongoDB"
                tags "Database"
            }
            novelOutbox = container "novel-outbox" {
                description "Публикует события об изменении состояния веб-новелл\n"
                technology "Spring Boot, CDC, Change Stream"
            }
            novelEvent = container "novel-event" {
                description "События о создании, изменении или удалении веб-новеллы"
                technology "Kafka"
                tags "Topic"
            }

            novelApi -> novelMongodb "Чтение/Запись веб-новелл и их событий"
            novelOutbox -> novelMongodb "Читает события веб-новелл"
            novelOutbox -> novelEvent "Публикует события веб-новелл"
        }

        search = softwareSystem "Поиск веб-новелл" {
            description "Поиск веб-новелл по различным фильтрам: автор, рейтинг, название и т.д."
            searchApi = container "search-api" {
                description "Поиск веб-новелл по различным фильтрам: автор, рейтинг, название и т.д."
                technology "Spring Boot, REST, API, Stateless"
            }
            searchElasticsearch = container "search-elasticsearch" {
                description "Хранит веб-новеллы в формате, удобном для поиска"
                technology "ElasticSearch"
                tags "Database"
            }
            searchIndexer = container "search-indexer" {
                description "Индексирует веб-новеллы"
                technology "Spring Boot"
            }

            searchIndexer -> novel.novelEvent "Читает события об изменении веб-новелл"
            searchIndexer -> user.userApi "Получает профиль автора"
            searchIndexer -> searchElasticsearch "Записывает веб-новеллы в индекс"
            searchApi -> searchElasticsearch "Выполняет поиск веб-новеллы по критериям"
        }

        rating = softwareSystem "Оценка веб-новелл" {
            description "Позволяет ставить оценки к произведениям"
            ratingApi = container "rating-api" {
                description "Позволяет ставить оценки к произведениям"
                technology "Spring Boot, REST, API, Stateless"
            }
            ratingCommand = container "rating-command" {
                description "Команды на добавление оценки произведения"
                technology "Kafka"
                tags "Topic"
            }
            ratingConsumer = container "rating-consumer" {
                description "Транзакционно и идемпотентно меняет текущую среднюю оценку веб-новеллы"
                technology "Spring Boot"
            }
            ratingEvent = container "rating-event" {
                description "События об изменении средней оценки произведения"
                technology "Kafka"
                tags "Topic"
            }
            ratingRedis = container "rating-redis" {
                description "• Хранит счетчик и сумму рейтинга\n• Хранит ключи идемпотентности"
                technology "Redis"
                tags "Database"
            }
            ratingApi -> ratingCommand "Отправляет команду на добавление оценки произведению"
            ratingApi -> ratingRedis "Получает среднюю оценку произведения по счетчику и сумме рейтинга"
            ratingConsumer -> ratingCommand "Читает команду на добавление оценки произведению"
            ratingConsumer -> ratingRedis "Транзакционно и идемпотентно увеличивает счетчик и сумму рейтинга"
            ratingConsumer -> ratingEvent "Публикует событие об изменении средней оценки произведения"
        }

        client -> gateway.gatewayApi
        gatewayToUser = gateway.gatewayApi -> user.userApi "Перенаправляет запросы; Получает публичный ключ для проверки JWT"
        gatewayToNovel = gateway.gatewayApi -> novel.novelApi "Перенаправляет запросы"
        gatewayToSearch = gateway.gatewayApi -> search.searchApi "Перенаправляет запросы"
        gatewayToRating = gateway.gatewayApi -> rating.ratingApi "Перенаправляет запросы"
    }

    views {
        systemLandscape landscape {
            include *
        }

        container gateway gateway {
            include *
            exclude "search -> *"
        }

        container user user {
            include *
            exclude "gateway -> *"
            include gatewayToUser
        }

        container novel novel {
            include *
            exclude "gateway -> *"
            include gatewayToNovel
        }

        container search search {
            include *
            exclude "gateway -> *"
            include gatewayToSearch
        }

        container rating rating {
            include *
            exclude "gateway -> *"
            include gatewayToRating
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
