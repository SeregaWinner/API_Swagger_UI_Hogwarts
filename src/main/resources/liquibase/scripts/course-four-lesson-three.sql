-- liquibase formatted sql
CREATE INDEX student_name_index ON students (name);
CREATE INDEX faculty_nc_index ON faculties (name, color);