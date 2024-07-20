package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.entity.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    List<Faculty> findAllByColor(String color);
}
