--liquibase formatted sql

--changeset whsv26:2024-11-18-001-comments-data
insert into comments(book_id, text) values
    (1, 'comment_1'),
    (1, 'comment_2')
;
