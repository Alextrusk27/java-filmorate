INSERT INTO MPA (name)
VALUES ('G');
INSERT INTO MPA (name)
VALUES ('PG');
INSERT INTO MPA (name)
VALUES ('PG-13');
INSERT INTO MPA (name)
VALUES ('R');
INSERT INTO MPA (name)
VALUES ('NC-17');

INSERT INTO GENRES (name)
VALUES ('Комедия');
INSERT INTO GENRES (name)
VALUES ('Драма');
INSERT INTO GENRES (name)
VALUES ('Мультфильм');
INSERT INTO GENRES (name)
VALUES ('Триллер');
INSERT INTO GENRES (name)
VALUES ('Документальный');
INSERT INTO GENRES (name)
VALUES ('Боевик');

INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY)
VALUES ('name_1', 'login_1', 'email-1@email.com', '1991-01-01'),
       ('name_2', 'login_2', 'email-2@email.com', '1992-02-02'),
       ('name_3', 'login_3', 'email-3@email.com', '1993-03-03'),
       ('name_4', 'login_4', 'email-4@email.com', '1994-04-04');

INSERT INTO FRIENDS (ID, FRIEND_ID, FRIEND_CONFIRMED)
VALUES (1, 2, FALSE),
       (1, 3, FALSE),
       (1, 4, FALSE),
       (2, 3, FALSE),
       (2, 4, FALSE);

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('title_1', 'description_1', '2001-01-01', 120, 1),
       ('title_2', 'description_2', '2002-02-02', 140, 2),
       ('title_3', 'description_3', '2003-03-03', 180, 3);

INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 1),
       (3, 2),
       (3, 3);

INSERT INTO LIKES (USER_ID, FILM_ID)
VALUES (1, 2),
       (1, 1),
       (2, 2),
       (4, 2),
       (4, 1);
