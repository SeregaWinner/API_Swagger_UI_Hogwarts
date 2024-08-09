package ru.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.entity.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudent_Id(Long id);
}
