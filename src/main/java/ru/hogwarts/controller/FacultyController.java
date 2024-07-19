package ru.hogwarts.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping("/{id}")
    public void editFaculty(@PathVariable long id, @RequestBody Faculty faculty) {
        facultyService.editFaculty(id, faculty);
    }

    @GetMapping("/{id}")
    public Faculty findFaculty(@PathVariable long id) {
        return facultyService.getFaculty(id);
    }

    @DeleteMapping("/{id}")
    public Faculty deleteFaculty(@PathVariable long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping(params = "color")
    public Collection<Faculty> findFacultiesByColor(@RequestParam String color) {
        return facultyService.findByColor(color);
    }

    @GetMapping(params = "colorOrName")
    public Collection<Faculty> findFacultiesByColorOrName(@RequestParam String colorOrName) {
        return facultyService.findByColorOrName(colorOrName);
    }

    @GetMapping("/{id}/students")
    public List<Student> findStudentsByacultyId(@PathVariable long id) {
        return facultyService.findStudentsByFacultyId(id);
    }
}
