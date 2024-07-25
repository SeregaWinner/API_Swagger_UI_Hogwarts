package ru.hogwarts.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.service.AvatarService;
import ru.hogwarts.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
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

    @GetMapping(params = "age")
    public Collection<Student> findStudents(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public List<Student> filterByAgeRange(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.filterByAgeRange(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findStudentsFaculty(@PathVariable long id) {
        return studentService.findStudentsFaculty(id);
    }

    @GetMapping("/{id}/avatar-from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromDb(id));
    }

    @GetMapping("/{id}/avatar-from-fs")
    public ResponseEntity<byte[]> getAvatarFromFs(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromFs(id));
    }

    private ResponseEntity<byte[]> buildResponseEntity(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);

    }

}
