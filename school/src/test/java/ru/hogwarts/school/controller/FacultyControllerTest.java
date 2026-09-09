package ru.hogwarts.school.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

// c
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired 
    private FacultyController facultyController;

    @Test 
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test 
    void getFacultyInfoTest() {
        String url = "http://localhost:" + port + "/faculty/1"; 
        String response = this.restTemplate.getForObject(url, String.class);
        assertThat(response).isNotNull();
    }

    @Test 
    void createFacultyTest() {
        String url = "http://localhost:" + port + "/faculty"; 
        String requestBody = "{\"name\":\"TestFaculty\",\"color\":\"Red\"}"; 
        String response = this.restTemplate.postForObject(url, requestBody, String.class);
        Assertions
            .assertThat(response)
            .isNotNull();
    }

    @Test 
    void updateFacultyTest() {
        String url = "http://localhost:" + port + "/faculty"; 
        String requestBody = "{\"id\":1,\"name\":\"UpdatedFaculty\",\"color\":\"Blue\"}"; 
        String response = this.restTemplate.postForObject(url, requestBody, String.class);
        Assertions
            .assertThat(response)
            .isNotNull();
    }

    @Test
    void deleteFacultyTest() {
        String url = "http://localhost:" + port + "/faculty/1"; 
        this.restTemplate.delete(url);
        String response = this.restTemplate.getForObject(url, String.class);
        Assertions
            .assertThat(response)
            .isNull();
    }

    @Test 
    void findFacultiesErrorTest() {
        String url = "http://localhost:" + port + "/faculty?color=NonExistentColor"; 
        String response = this.restTemplate.getForObject(url, String.class);
        Assertions
            .assertThat(response)
            .isEqualTo("[]");
    }


}
