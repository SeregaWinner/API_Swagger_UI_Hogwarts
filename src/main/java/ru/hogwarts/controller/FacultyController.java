package ru.hogwarts.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.service.FacultyService;

import java.util.Collection;

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
        return facultyService.findFaculty(id);
    }

    @DeleteMapping("/{id}")
    public Faculty deleteFaculty(@PathVariable long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> findFaculties(@RequestParam String color) {
        return facultyService.findByColor(color);
    }
}
