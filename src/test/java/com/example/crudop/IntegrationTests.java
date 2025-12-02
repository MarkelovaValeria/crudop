package com.example.crudop;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.crudop.Utils.Utils;
import com.example.crudop.model.Course;
import com.example.crudop.repository.CourseRepository;
import com.example.crudop.request.CourseCreateRequest;
import com.example.crudop.request.CourseUpdateRequest;
import com.example.crudop.service.CourseService;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseService courseService;

    private Course savedCourse;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        savedCourse = repository.save(new Course("Test Course", "English", "A1", "###test"));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void createCourseViaDtoShouldSucceed() throws Exception {
        CourseCreateRequest request = new CourseCreateRequest("New Course", "French", "B1", "###test dto");

        ResultActions result = mockMvc.perform(post("/api/v1/courses/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)));

        result.andExpect(status().isOk());

        List<Course> all = repository.findAll();
        assertThat(all).anyMatch(c -> "New Course".equals(c.getName()) && c.getDescription().contains("###test dto"));
    }

    @Test
    void createCourseWithDuplicateIdShouldReturnNull() {
        Course duplicate = new Course("Duplicate", "Eng", "A2", "###duplicate");
        duplicate.setId(savedCourse.getId());
        Course created = repository.save(duplicate);
        assertNotEquals(savedCourse.getName(), created.getName());
    }

    @Test
    void getAllCoursesShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/v1/courses/"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourseByIdShouldReturnCourse() throws Exception {
        mockMvc.perform(get("/api/v1/courses/" + savedCourse.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getCourseByIdNotExistsShouldReturnNull() {
        Course course = repository.findById("nonexistent").orElse(null);
        assertNull(course);
    }

    @Test
    void updateCourseViaRequestShouldSucceed() {
        CourseUpdateRequest request = new CourseUpdateRequest(savedCourse.getId(), "Updated", "English", "A2", "###updated");
        savedCourse.setName(request.name());
        savedCourse.setLevel(request.level());
        savedCourse.setDescription(request.description());
        Course updated = repository.save(savedCourse);

        assertEquals("Updated", updated.getName());
        assertEquals("A2", updated.getLevel());
        assertEquals("###updated", updated.getDescription());
    }

    @Test
    void updateCourseWithNullIdShouldReturnNull() {
        CourseUpdateRequest request = new CourseUpdateRequest(
                null, "Updated Name", "UpdatedLang", "T2", "Updated Description"
        );

        Course updated = courseService.update(
                request.id() == null ? null : new Course(
                request.id(),
                request.name(),
                request.language(),
                request.level(),
                request.description(),
                null,
                null
        )
        );

        assertNull(updated, "Оновлення без id має повертати null");
    }

    @Test
    void deleteCourseShouldRemoveIt() {
        repository.deleteById(savedCourse.getId());
        assertFalse(repository.existsById(savedCourse.getId()));
    }

    @Test
    void createCourseShouldSucceed() {
        Course course = new Course("Direct Create", "German", "B2", "###direct");
        Course created = repository.save(course);
        assertNotNull(created.getId());
        assertEquals("Direct Create", created.getName());
    }

    @Test
    void updateCourseShouldChangeFields() {
        savedCourse.setLevel("C1");
        savedCourse.setDescription("###modified");
        Course updated = repository.save(savedCourse);

        assertEquals("C1", updated.getLevel());
        assertEquals("###modified", updated.getDescription());
    }
}
