package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.entity.Student;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.exception.FacultyNotFoundException;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        return facultyRepository.findById(id).
                orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public void editFaculty(long id, Faculty faculty) {
        Faculty oldFaculty = facultyRepository.findById(id).
                orElseThrow(() -> new FacultyNotFoundException(id));
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id).
                orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return faculty;
    }

    public List<Faculty> findByColor(String color) {
        return facultyRepository.findAllByColor(color);

    }

    public Collection<Faculty> findByColorOrName(String colorOrName) {
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(colorOrName,colorOrName);
    }

    public List<Student> findStudentsByFacultyId(long id) {
        return studentRepository.findAllByFaculty_Id(id);
    }
}
