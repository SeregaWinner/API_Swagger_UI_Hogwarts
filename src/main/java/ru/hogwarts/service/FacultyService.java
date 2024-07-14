package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.exception.FucultyNotFoundException;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        return facultyRepository.findById(id).
                orElseThrow(() -> new FucultyNotFoundException(id));
    }

    public void editFaculty(long id, Faculty faculty) {
        Faculty oldFaculty = facultyRepository.findById(id).
                orElseThrow(() -> new FucultyNotFoundException(id));
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id).
                orElseThrow(() -> new FucultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return faculty;
    }

    public List<Faculty> findByColor(String color) {
        return facultyRepository.findAllByColor(color);

    }
}
