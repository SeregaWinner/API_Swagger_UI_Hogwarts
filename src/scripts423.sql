
SELECT students.name, students.age, faculties.name
FROM students
INNER JOIN faculties ON students.faculty_id = faculty.id
SELECT students.name, students.age
FROM students
INNER JOIN avatars ON avatars.student_id = student.id