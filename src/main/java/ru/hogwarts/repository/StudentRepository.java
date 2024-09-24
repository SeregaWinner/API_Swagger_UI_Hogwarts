package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAllByFaculty_Id(long id);
    @Query(value = "SELECT count(*) AS count FROM students", nativeQuery = true)
    long getCountStudents();

    @Query(value = "SELECT AVG(age) AS avg FROM students", nativeQuery = true)
    double getAvgAgeStudents();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getDescFiveStudents();
}
