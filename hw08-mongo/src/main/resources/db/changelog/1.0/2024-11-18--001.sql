--liquibase formatted sql

--changeset whsv26:2024-11-18-001-comments
create table comments (
    id bigserial,
    book_id bigint references books (id) on delete cascade,
    text text,
    primary key (id)
);
