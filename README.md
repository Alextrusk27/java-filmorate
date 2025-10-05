# java-filmorate

## Database
![](docs/images/db_scheme.png)

 [Link to scheme on dbdiagram.io](https://dbdiagram.io/d/Filmorate-DB-68d3c7f5d2b621e422b50ba8)

### SQL request examples

1. *Get all users*
```SQL
SELECT *
FROM users;
```
2. *Get user by ID (7)*
```SQL
SELECT *
FROM users
WHERE id = 7;
```
3. *Get friends by ID (3)*
```SQL
SELECT *
FROM users AS u
WHERE u.id IN (
    SELECT f.friend_id
    FROM friends AS f
    WHERE f.id = 3
);
```
4. *Get common friends by ID (16) and ID (23)*
```SQL
SELECT *
FROM users AS u
WHERE u.id in (
    SELECT f.friend_id
    FROM friends AS f
    WHERE f.id IN (16, 23)
    GROUP BY f.friend_id
    HAVING COUNT(DISTINCT f.id) = 2
);
```
5. *Get all films*
```SQL
SELECT *
FROM films;
```
6. *Get film by ID (10)*
```SQL
SELECT *
FROM films
WHERE id = 10;
```
7. *Get top (20) popular films*
```SQL
SELECT *
FROM films as f
WHERE f.id IN (
    SELECT l.film_id
    FROM likes AS l
    GROUP BY l.film_id
    ORDER BY COUNT(l.film_id) DESC
    LIMIT 20
);
```