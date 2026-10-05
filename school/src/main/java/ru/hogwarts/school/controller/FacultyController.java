package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.RestController;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.readFaculty(id);
        if (faculty == null) {
            logger.warn("Faculty with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.updateFaculty(faculty);
        if (foundFaculty == null) {
            logger.debug("Faculty with id {} not found", faculty.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        logger.info("Was invoked method for delete faculty with id = {}", id);
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            logger.debug("Received request to find faculties by color: {}", color);
            return ResponseEntity.ok(facultyService.findByColor(color));
        }
        logger.info("Returning empty list of faculties");
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<Faculty>> findByCM(@RequestParam String name,
                                                        @RequestParam String color) {
        logger.debug("Received request to find faculties by name and color: {} {}", name, color);
        logger.info("Returning list of faculties");
        return ResponseEntity.ok(facultyService.findByNameNC(name, color));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Set<Student>> getFacultyStudents(@PathVariable Long id) {
        Faculty faculty = facultyService.readFaculty(id);
        logger.info("Returning list of students for faculty with id = {}", id);
        return ResponseEntity.ok(faculty.getStudents());
    }
}
