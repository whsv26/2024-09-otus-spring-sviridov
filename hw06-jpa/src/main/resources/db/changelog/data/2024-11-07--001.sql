--liquibase formatted sql

--changeset whsv26:2024-11-07-001-authors-data
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

--changeset whsv26:2024-11-07-001-genres-data
insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

--changeset whsv26:2024-11-07-001-books-data
insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

--changeset whsv26:2024-11-07-001-books_genres-data
insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);
