package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.exception.FacultyNotFoundException;
import ru.hogwarts.exception.StudentNotFoundException;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for \"addStudent\"");
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("There is not faculty with id = " + student.getFaculty().getId());
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        student.setFaculty(faculty);
        student.setId(null);
        logger.debug("Was transmitted \"student\"={} in repository from method \"addStudent\"", student);
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        logger.info("Was invoked method for \"getStudent\"");
        logger.debug("Was request \"studentRepository.findById(id)\"={} in repository from method \"getStudent\"", id);
        return studentRepository.findById(id).
                orElseThrow(() -> {
                    logger.error("There is not student with id = " + id);
                    return new StudentNotFoundException(id);
                });
    }

    public Student editStudent(long id, Student student) {
        logger.info("Was invoked method for \"editStudent\"");
        logger.debug("Was request \"studentRepository.deleteById(id)\"={} " +
                "in repository from method \"editStudent\"", id);
        Student oldStudent = studentRepository.findById(id).
                orElseThrow(() -> {
                    logger.error("There is not student with id = " + id);
                    return new StudentNotFoundException(id);
                });
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("There is not faculty with id = " + student.getFaculty().getId());
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        logger.debug("Was transmitted \"student\"={} in repository from method \"editStudent\"", student);
        return studentRepository.save(oldStudent);
    }

    public Student deleteStudent(long id) {
        logger.info("Was invoked method for \"deleteStudent\"");
        Student student = studentRepository.findById(id).
                orElseThrow(() -> new StudentNotFoundException(id));
        logger.debug("Was request \"studentRepository.deleteById(id)\"={} " +
                "in repository from method \"deleteStudent\"", id);
        studentRepository.delete(student);
        return student;
    }

    public List<Student> findByAge(int age) {
        logger.info("Was invoked method for \"findByAge\"");
        logger.debug("Was request \"studentRepository.findAllByAge(age)\"={} " +
                "in repository from method \"getStudent\"", age);
        return studentRepository.findAllByAge(age);

    }

    public List<Student> filterByAgeRange(int minAge, int maxAge) {
        logger.info("Was invoked method for \"filterByAgeRange\"");
        logger.info("Was request \"studentRepository.findByAgeBetween(minAge, maxAge)\"={},{} " +
                "in repository from method \"filterByAgeRange\"", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findStudentsFaculty(long id) {
        logger.info("Was invoked method for \"findStudentsFaculty\"");
        logger.debug("Was request \"getStudent(id).getFaculty()\"={} " +
                "in repository from method \"findStudentsFaculty\"", id);
        return getStudent(id).getFaculty();
    }

    public long getCountStudents() {
        logger.info("Was invoked method for \"getCountStudents\"");
        return studentRepository.getCountStudents();
    }

    public double getAvgAgeStudents() {
        logger.info("Was invoked method for \"getAvgAgeStudents\"");
        return studentRepository.getAvgAgeStudents();
    }

    public List<Student> getDescFiveStudents() {
        logger.info("Was invoked method for \"getDescFiveStudents\"");
        return studentRepository.getDescFiveStudents();
    }

    public List<String> getAllStudentWithNameOnLetterA() {
        return studentRepository.findAll().stream()
                .parallel()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAgeMediumAllStudent() {
        return studentRepository.findAll().stream()
                .parallel()
                .collect(Collectors.averagingInt(Student::getAge));
    }

    public long getNumberTypeInt() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
    }
}
