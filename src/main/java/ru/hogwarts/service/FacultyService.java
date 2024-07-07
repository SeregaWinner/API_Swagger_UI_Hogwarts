package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.model.FacultyHogwarts;

import java.util.*;

@Service
public class FacultyService {
    private final Map<Long, FacultyHogwarts> studentsFaculty = new HashMap<>();
    private long lastId = 0;

    public FacultyHogwarts addFaculty(FacultyHogwarts faculty) {
        faculty.setId(lastId++);
        studentsFaculty.put(faculty.getId(), faculty);
        return faculty;
    }

    public FacultyHogwarts findFaculty(long id) {
        return studentsFaculty.get(id);
    }

    public FacultyHogwarts editFaculty(FacultyHogwarts faculty) {
        if (!studentsFaculty.containsKey(faculty.getId())) {
            return null;
        }
        studentsFaculty.put(faculty.getId(), faculty);
        return faculty;
    }

//    public FacultyHogwarts deleteFaculty(long id) {
//        return studentsFaculty.remove(id);
//    }
//
//    public Collection<FacultyHogwarts> findByColor(String color) {
//        ArrayList<FacultyHogwarts> result = new ArrayList<>();
//        for (FacultyHogwarts faculty : studentsFaculty.values()) {
//            if (Objects.equals(faculty.getColor(), color)) {
//                result.add(faculty);
//            }
//        }
//        return result;
//    }
}
