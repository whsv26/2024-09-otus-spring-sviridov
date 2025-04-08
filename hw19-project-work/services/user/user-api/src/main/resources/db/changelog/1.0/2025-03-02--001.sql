--liquibase formatted sql

--changeset whsv26:2025-03-02-001-users
create table users (
    id uuid primary key,
    username varchar(255) unique,
    password varchar(255),
    authorities text
);
