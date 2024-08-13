ALTER TABLE students ADD CONSTRAINT age_constraint1 CHECK (age > 16)
ALTER TABLE students ALTER COLUMN name SET NOT NULL
ALTER TABLE students ADD CONSTRAINT name_unique UNIQUE (name)
ALTER TABLE faculties ADD CONSTRAINT name_color_unique UNIQUE (name, color)
ALTER TABLE students ALTER COLUMN age SET default 20