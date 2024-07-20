package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.entity.Students;

public interface StudentRepository extends JpaRepository<Students, Long> {
}
