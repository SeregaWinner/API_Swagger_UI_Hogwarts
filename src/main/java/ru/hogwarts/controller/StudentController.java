package ru.hogwarts.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.entity.Student;
import ru.hogwarts.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public void editStudent(@PathVariable long id, @RequestBody Student student) {
        studentService.editStudent(id, student);
    }

    @GetMapping("/{id}")
    public Student getStudentInfo(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @DeleteMapping("/{id}")
    public Student deleteStudent(@PathVariable long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping
    public Collection<Student> findStudents(@RequestParam int age) {
        return studentService.findByAge(age);
    }

}
