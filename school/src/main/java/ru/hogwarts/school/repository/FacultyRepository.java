package ru.hogwarts.school.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color);
}
