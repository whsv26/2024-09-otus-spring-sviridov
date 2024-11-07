--liquibase formatted sql

--changeset whsv26:2024-11-07-001-authors
create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

--changeset whsv26:2024-11-07-001-genres
create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

--changeset whsv26:2024-11-07-001-books
create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

--changeset whsv26:2024-11-07-001-books-genres
create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);