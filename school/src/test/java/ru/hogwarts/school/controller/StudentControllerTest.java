package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired 
    private StudentController studentController;

    private Student createStudent(String name, int age) {
        Student student = new Student(name, age);
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }

    @Test 
    void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    void shouldReturn404WhenStudentNotFound() {
        Assertions
            .assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/student/99999", Student.class).getStatusCode())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createStudentTest() {
        Student newStudent = new Student("TestName", 10); 
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",    
                newStudent,     
                Student.class   
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("TestName");
        assertThat(response.getBody().getAge()).isEqualTo(10);
        assertThat(response.getBody().getId()).isNotNull(); 
    }

    @Test
    void updateStudentTest() {
        Student createdStudent = createStudent("Гермиона Грейнджер", 19);
        Student studentToUpdate = new Student("Гермиона Грейнджер", 20);
        studentToUpdate.setId(createdStudent.getId());

        RequestEntity<Student> requestEntity = RequestEntity
                .put(URI.create("http://localhost:" + port + "/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(studentToUpdate);

        ResponseEntity<Student> response = restTemplate.exchange(
                requestEntity,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdStudent.getId());
        assertThat(response.getBody().getName()).isEqualTo("Гермиона Грейнджер");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void deleteStudentTest() {
        Student createdStudent = createStudent("Удаляемый студент", 22);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + createdStudent.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void findStudentTest() {
        createStudent("Студент для поиска", 20);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student?age=20",
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].getAge()).isEqualTo(20);
    }

}
