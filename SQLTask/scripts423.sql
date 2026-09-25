SELECT 
    s.name AS student_name, 
    s.age, 
    f.name AS faculty_name
FROM student s
JOIN faculty f ON s.faculty_id = f.id;

SELECT 
    s.name, 
    s.avatar_url
FROM student s
WHERE s.avatar_url IS NOT NULL;