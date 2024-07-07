package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.model.StudentsHogwarts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, StudentsHogwarts> students = new HashMap<>();
    private long lastId = 0;


    public StudentsHogwarts addStudent(StudentsHogwarts student) {
        student.setId(lastId++);
        students.put(student.getId(), student);
        return student;
    }

    public StudentsHogwarts findStudent(long id) {
        return students.get(id);
    }

    public StudentsHogwarts editStudent(StudentsHogwarts student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public StudentsHogwarts deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<StudentsHogwarts> findByAge(int age) {
        ArrayList<StudentsHogwarts> result = new ArrayList<>();
        for (StudentsHogwarts student : students.values()) {
            if (student.getAge() == age) {
                result.add(student);
            }
        }
        return result;
    }
}
