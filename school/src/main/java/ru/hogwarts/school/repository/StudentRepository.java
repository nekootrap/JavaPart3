package ru.hogwarts.school.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.Query;


public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int min, int max);


    @Query("SELECT COUNT(*) FROM Student")
    long countAllStudents();

    @Query("SELECT COALESCE(AVG(s.age), 0.0) FROM Student s")
    double getAverageAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}

