insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(book_id, text) values
    (1, 'comment_1'),
    (1, 'comment_2')
;

insert into users(username, password, authorities) values
    ('admin', '$2a$04$LmAy3AXbt2TWTCOqKbuuue76fhSs6pplVHyrN0HKXsm6czERX.5Su', 'ROLE_ADMIN,ROLE_EDITOR')
;
