--liquibase formatted sql
--changeset polina:1
CREATE INDEX idx_student_name ON student (name);

--liquibase formatted sql
--changeset polina:2
CREATE INDEX idx_faculty_name_color ON faculty (name, color);