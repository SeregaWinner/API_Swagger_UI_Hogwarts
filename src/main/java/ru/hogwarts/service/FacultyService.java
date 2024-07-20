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
        faculty.setId(lastId++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        if (!faculties.containsKey(id)) {
            throw new FucultyNotFoundException(id);
        }
        return faculties.get(id);
    }

    public void editFaculty(long id, Faculty faculty) {
        if (!faculties.containsKey(id)) {
            throw new FucultyNotFoundException(id);
        }
        faculty.setId(id);
        faculties.replace(id, faculty);
    }

    public Faculty deleteFaculty(long id) {
        if (!faculties.containsKey(id)) {
            throw new FucultyNotFoundException(id);
        }
        return faculties.remove(id);
    }

    public Collection<Faculty> findByColor(String color) {
        ArrayList<Faculty> result = new ArrayList<>();
        for (Faculty faculty : faculties.values()) {
            if (Objects.equals(faculty.getColor(), color)) {
                result.add(faculty);
            }
        }
        return result;
    }
}
