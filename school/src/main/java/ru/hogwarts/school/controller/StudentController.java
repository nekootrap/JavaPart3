package ru.hogwarts.school.controller;

import java.util.Collections;
import java.util.Collection;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentService studentService;
     private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/count")
    public long getCount() {
        logger.info("Was invoked method for get count of students");
        return studentRepository.countAllStudents();
    }

    @GetMapping("/average-age")
    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.getAverageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFive() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findLastFiveStudents();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        logger.info("Was invoked method for get student info");
        Student student = studentService.readStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        logger.info("Was invoked method for create student");
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        logger.info("Was invoked method for update student");
        Student foundStudent = studentService.updateStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.error("There is not student with id = " + id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) int age) {
        if (age > 0) {
            logger.debug("Received request to find student by age: {}", age);
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<Student>> findStudentsByMMAge(@RequestParam int min,
                                                                    @RequestParam int max) {
        logger.debug("Received request to find students by age range: {} - {}", min, max);
        return ResponseEntity.ok(studentService.findByAgeMM(min, max));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        logger.info("Was invoked method for get faculty of student with id = " + id);
        Student student = studentService.readStudent(id);
        return ResponseEntity.ok(student.getFaculty());
    }

    @GetMapping("/students/print-parallel")
    public ResponseEntity<String> printParallel() {
        List<Student> students = studentRepository.findAll();

        System.out.println("Main Thread: " + students.get(0).getName());
        System.out.println("Main Thread: " + students.get(1).getName());

        new Thread(() -> {
            System.out.println("Thread-1: " + students.get(2).getName());
            System.out.println("Thread-1: " + students.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println("Thread-2: " + students.get(4).getName());
            System.out.println("Thread-2: " + students.get(5).getName());
        }).start();

        return ResponseEntity.ok("Потоки запущены");
    }

    private synchronized void printStudentName(String name) {
        System.out.println(Thread.currentThread().getName() + ": " + name);
    }

    @GetMapping("/students/print-synchronized")
    public ResponseEntity<String> printSynchronized() {
        List<Student> students = studentRepository.findAll();

        printStudentName(students.get(0).getName());
        printStudentName(students.get(1).getName());

        new Thread(() -> {
            printStudentName(students.get(2).getName());
            printStudentName(students.get(3).getName());
        }).start();

        new Thread(() -> {
            printStudentName(students.get(4).getName());
            printStudentName(students.get(5).getName());
        }).start();

        return ResponseEntity.ok("Синхронизированные потоки запущены");
    }

}
