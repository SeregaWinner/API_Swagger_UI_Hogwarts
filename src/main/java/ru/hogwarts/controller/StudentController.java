package ru.hogwarts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.model.StudentsHogwarts;
import ru.hogwarts.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentsHogwarts> getStudentInfo(@PathVariable Long id) {
        StudentsHogwarts studentsHogwarts = studentService.findStudent(id);
        if (studentsHogwarts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentsHogwarts);
    }

    @PostMapping
    public StudentsHogwarts createStudent(@RequestBody StudentsHogwarts studentsHogwarts) {
        return studentService.addStudent(studentsHogwarts);
    }

    @PutMapping
    public ResponseEntity<StudentsHogwarts> editStudent(@RequestBody StudentsHogwarts studentsHogwarts) {
        StudentsHogwarts foundStudent = studentService.editStudent(studentsHogwarts);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

//    @DeleteMapping
//    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
//        studentService.deleteStudent(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<Collection<StudentsHogwarts>> findStudents(@RequestParam(required = false) int age) {
//        if (age > 0) {
//            return ResponseEntity.ok(studentService.findByAge(age));
//        }
//        return ResponseEntity.ok(Collections.emptyList());
//    }

}
