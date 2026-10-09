ALTER TABLE student 
ADD CONSTRAINT chk_student_age CHECK (age > 16);
ALTER TABLE student
ADD CONSTRAINT chk_studentName CHECK (name IS NOT NULL AND name <> '');
AlTER TABLE student
ADD CONSTRAINT chk_student_uniqueName UNIQUE (name);

ALTER TABLE faculty 
ADD CONSTRAINT uq_faculty_name_color UNIQUE (name, color);

ALTER TABLE student 
ALTER COLUMN age SET DEFAULT 20;

SELECT * FROM student;
SELECT * FROM faculty;

