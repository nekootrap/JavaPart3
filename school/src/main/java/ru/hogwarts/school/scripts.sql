SELECT * FROM student;

SELECT * FROM student WHERE age BETWEEN 10 AND 20;

SELECT name FROM student;

SELECT * FROM student WHERE name LIKE '%о%';

SELECT * FROM student WHERE age < id;

SELECT * FROM student ORDER BY age;

SELECT * FROM faculty

UPDATE student SET faculty_id = 1 WHERE id IN (1, 2);

UPDATE student SET faculty_id = 2 WHERE id = 3;

UPDATE student SET faculty_id = 3 WHERE id IN (4, 5);