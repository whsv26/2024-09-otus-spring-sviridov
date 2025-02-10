create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments (
    id bigserial,
    book_id bigint references books (id) on delete cascade,
    text text,
    primary key (id)
);

create table key_mapping (
    source_id varchar(255),
    target_id bigserial,
    type text,
    primary key (source_id, type)
);
