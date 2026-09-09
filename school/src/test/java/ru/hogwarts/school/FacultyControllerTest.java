package ru.hogwarts.school;


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.mockito.InjectMocks;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.model.Faculty;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import java.util.Optional;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean 
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void getFacultyInfoErrorTest() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty/99999")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test 
    public void createFacultyTest() throws Exception {
        final String name = "TestFaculty";
        final Long id = 1L;
        final String color = "Red";

        JSONObject facultyObj = new JSONObject();
        facultyObj.put("name", name);
        facultyObj.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/faculty")
                .content(facultyObj.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test 
    public void updateFacultyTest() throws Exception {
        final String name = "UpdatedFaculty";
        final Long id = 1L;
        final String color = "Blue";

        JSONObject facultyObj = new JSONObject();
        facultyObj.put("id", id);
        facultyObj.put("name", name);
        facultyObj.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/faculty")
                .content(facultyObj.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test 
    public void deleteFacultyTest() throws Exception {
        final Long id = 1L;

        when(facultyRepository.findById(id)).thenReturn(Optional.of(new Faculty()));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/faculty/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test 
    public void findFacultiesTest() throws Exception {
        final String color = "Red";

        when(facultyService.findByColor(color)).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty?color={color}", color)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
}
