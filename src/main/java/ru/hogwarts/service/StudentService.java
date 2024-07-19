package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.exception.StudentNotFoundException;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student addStudent(Student student) {
        Faculty faculty = null;
        if(student.getFaculty()!= null && student.getFaculty().getId() != null){
             faculty =facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(()-> new StudentNotFoundException(student.getFaculty().getId()));
        }
        student.setFaculty(faculty);
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
        Faculty faculty = null;
        if(student.getFaculty()!= null && student.getFaculty().getId() != null){
            faculty =facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(()-> new StudentNotFoundException(student.getFaculty().getId()));
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
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

    public List<Student> filterByAgeRange(int maxAge, int minAge) {
        return studentRepository.findByAgeBetween(maxAge, minAge);
    }

    public Faculty findStudentsFaculty(long id) {
        return getStudent(id).getFaculty();
    }
}
