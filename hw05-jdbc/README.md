Создать приложение, каталог книг в библиотеке

## Цель

Использовать возможности Spring JDBC и spring-boot-starter-jdbc для подключения к реляционным базам данных

## Результат

Приложение с хранением данных в реляционной БД, которое в дальнейшем будем развивать

## Описание

Это домашнее задание выполняется НЕ на основе предыдущего.

- Использовать Spring JDBC и реляционную базу H2
- Предусмотреть сущности авторов, книг и жанров. Каждая должна храниться в своей таблице
- Предполагаются отношения многие-к-одному (у книги один автор, но у автора может быть несколько книг, то же касается книг и жанров)
- Интерфейс выполняется на Spring Shell (CRUD книги плюс, как минимум, операции вывода всех авторов и жанров)
- Создание и инициализация схемы БД должно происходить через schema.sql + data.sql или через систему миграций (Liquibase/Flyway)
- С помощью @JdbcTest сделать интеграционные тесты всех методов дао книг (со встроенной БД)

Дополнительные требования к выполнению работы:
- Проблема N+1 должна быть решена
- Использовать NamedParametersJdbcTemplate
- Не делать абстрактных или обобщенных Dao
- Не делать абстрактных или обобщенных сущностей
- Не делать двунаправленных связей (в книге автор, в авторе книги)
- Отношения многие-ко-многим для одной сущности (опциональное усложнение)

Ссылки на заготовки:

- Базовый вариант: https://github.com/OtusTeam/Spring/tree/master/templates/hw05-jdbc-simple
- Вариант с опциональным усложнением: https://github.com/OtusTeam/Spring/tree/master/templates/hw05-jdbc-hard