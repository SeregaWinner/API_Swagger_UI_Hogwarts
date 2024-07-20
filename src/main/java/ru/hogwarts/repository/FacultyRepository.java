package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
}
