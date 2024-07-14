package ru.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.exception.StudentNotFoundException;
import ru.hogwarts.entity.Students;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Students> students = new HashMap<>();
    private long lastId = 1;


    public Students addStudent(Students student) {
        student.setId(lastId++);
        students.put(student.getId(), student);
        return student;
    }

    public Students findStudent(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return students.get(id);
    }

    public void editStudent(long id, Students student) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        student.setId(id);
        students.replace(id, student);
    }

    public Students deleteStudent(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return students.remove(id);
    }

    public Collection<Students> findByAge(int age) {
        ArrayList<Students> result = new ArrayList<>();
        for (Students student : students.values()) {
            if (student.getAge() == age) {
                result.add(student);
            }
        }
        return result;
    }
}
