--liquibase formatted sql

--changeset whsv26:2025-03-02-001-authors
create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

--changeset whsv26:2025-03-02-001-genres
create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

--changeset whsv26:2025-03-02-001-books
create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

--changeset whsv26:2025-03-02-001-books-genres
create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

--changeset whsv26:2025-03-02-001-comments
create table comments (
    id bigserial primary key,
    book_id bigint references books (id) on delete cascade,
    text text
);

--changeset whsv26:2025-03-02-001-users
create table users (
    id bigserial primary key,
    username varchar(255) unique,
    password varchar(255),
    authorities text
);
