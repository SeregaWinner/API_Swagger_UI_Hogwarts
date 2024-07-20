package ru.hogwarts.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.entity.Students;
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
    public Students createStudent(@RequestBody Students student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public void editStudent(@PathVariable long id, @RequestBody Students student) {
        studentService.editStudent(id, student);
    }

    @GetMapping("/{id}")
    public Students getStudentInfo(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @DeleteMapping("/{id}")
    public Students deleteStudent(@PathVariable long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping
    public Collection<Students> findStudents(@RequestParam int age) {
        return studentService.findByAge(age);
    }

}
