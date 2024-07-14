package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.exception.FucultyNotFoundException;
import ru.hogwarts.entity.Student;
import ru.hogwarts.exception.StudentNotFoundException;
import ru.hogwarts.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).
                orElseThrow(() -> new StudentNotFoundException(id));
    }

    public void editStudent(long id, Student student) {
        Student oldStudent = studentRepository.findById(id).
                orElseThrow(() -> new StudentNotFoundException(id));
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        studentRepository.save(oldStudent);
    }

    public Student deleteStudent(long id) {
        Student student = studentRepository.findById(id).
                orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;
    }

    public List<Student> findByAge(int ege) {
        return studentRepository.findAllByAge(ege);

    }
}
