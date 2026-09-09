package ru.hogwarts.school;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.repository.StudentRepository;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.mockito.InjectMocks;
import ru.hogwarts.school.service.StudentService;
import org.json.JSONObject;
import ru.hogwarts.school.model.Student;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void createStudentTest() throws Exception {
        final String name = "TestName";
        final Long id = 1L;
        final int age = 20;

        JSONObject studentObj = new JSONObject();
        studentObj.put("name", name);
        studentObj.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/student")
                .content(studentObj.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test 
    public void getStudentInfoTest() throws Exception {
        final String name = "TestName";
        final Long id = 1L;
        final int age = 20;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/student/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test 
    public void updateStudentTest() throws Exception {
        final String name = "UpdatedName";
        final Long id = 1L;
        final int age = 25;

        JSONObject studentObj = new JSONObject();
        studentObj.put("id", id);
        studentObj.put("name", name);
        studentObj.put("age", age);

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setName(name);
        updatedStudent.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(updatedStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/student")
                .content(studentObj.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test 
    public void deleteStudentTest() throws Exception {
        final Long id = 1L;

        Student student = new Student();
        student.setId(id);
        student.setName("TestName");
        student.setAge(20);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/student/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test 
    public void findStudentsErrorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/student?age=99999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

}
