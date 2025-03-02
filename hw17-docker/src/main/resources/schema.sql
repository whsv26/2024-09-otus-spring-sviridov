create table authors (
    id bigserial primary key,
    full_name varchar(255)
);

create table genres (
    id bigserial primary key,
    name varchar(255)
);

create table books (
    id bigserial primary key,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments (
    id bigserial primary key,
    book_id bigint references books (id) on delete cascade,
    text text
);

create table users (
    id bigserial primary key,
    username varchar(255) unique,
    password varchar(255),
    authorities text
);
