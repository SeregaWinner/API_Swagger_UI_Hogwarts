package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for \"addFaculty\"");
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        logger.info("Was invoked method for \"getFaculty\"");
        return facultyRepository.findById(id).
                orElseThrow(() -> {
                    logger.error("There is not faculty with id = " + id);
                    return new FacultyNotFoundException(id);
                });
    }

    public void editFaculty(long id, Faculty faculty) {
        logger.info("Was invoked method for \"editFaculty\"");
        Faculty oldFaculty = facultyRepository.findById(id).
                orElseThrow(() -> {
                    logger.error("There is not faculty with id = " + id);
                    return new FacultyNotFoundException(id);
                });
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(long id) {
        logger.info("Was invoked method for \"deleteFaculty\"");
        Faculty faculty = facultyRepository.findById(id).
                orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return faculty;
    }

    public List<Faculty> findByColor(String color) {
        logger.info("Was invoked method for \"findAllByColor\"");
        return facultyRepository.findAllByColor(color);

    }

    public Collection<Faculty> findByColorOrName(String colorOrName) {
        logger.info("Was invoked method for \"findByNameOrColor\"");
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }

    public List<Student> findStudentsByFacultyId(long id) {
        logger.info("Was invoked method for \"findStudentsByFacultyId\"");
        return studentRepository.findAllByFaculty_Id(id);
    }
}
