MERGE INTO genres (id, name)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO mpa (id, name)
    values (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

INSERT INTO films (name, description, releaseDate, duration, mpa) VALUES ('Форсаж', 'Описание форсажа', '2000-01-01', 100, 1);
INSERT INTO films (name, description, releaseDate, duration, mpa) VALUES ('Форсаж 2', 'Описание форсажа 2', '2002-01-01', 100, 1);
INSERT INTO films (name, description, releaseDate, duration, mpa) VALUES ('Форсаж 3', 'Описание форсажа 3', '2003-01-01', 100, 1);

INSERT INTO USERS (email, login, name, birthday) VALUES ('email1@ya.ru', 'Мамкин_Тащер', 'Санек', '2000-01-01');
INSERT INTO USERS (email, login, name, birthday) VALUES ('email2@ya.ru', 'Мамкин_Нагибатор', 'Олежа', '2002-01-01');
INSERT INTO USERS (email, login, name, birthday) VALUES ('email3@ya.ru', 'Денди_мега_хук', 'Даниил', '2002-01-01');

