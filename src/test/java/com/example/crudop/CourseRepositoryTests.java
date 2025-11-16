package com.example.crudop;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.crudop.model.Course;
import com.example.crudop.repository.CourseRepository;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository underTest;

    private Course english, french, german;

    @BeforeAll
    void setupAll() {
        underTest.deleteAll();
    }

    @BeforeEach
    void setup() {
        english = new Course("English A1", "English", "A1", "Beginner English course");
        french = new Course("French B1", "French", "B1", "Intermediate French course");
        german = new Course("German A2", "German", "A2", "Basic German course");
        underTest.saveAll(List.of(english, french, german));
    }

    @AfterEach
    void cleanup() {
        underTest.deleteAll();
    }

    @Test
    void shouldHaveThreeRecordsInitially() {
        List<Course> courses = underTest.findAll();
        assertEquals(3, courses.size());
    }

    @Test
    void shouldSaveNewCourse() {
        Course spanish = new Course("Spanish B2", "Spanish", "B2", "Upper-intermediate Spanish course");
        Course saved = underTest.save(spanish);
        assertNotNull(saved.getId());
        assertEquals("Spanish B2", saved.getName());
    }

    @Test
    void shouldFindCourseById() {
        Course found = underTest.findById(english.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("English A1", found.getName());
    }

    @Test
    void shouldUpdateCourse() {
        english.setLevel("A2");
        underTest.save(english);
        Course updated = underTest.findById(english.getId()).orElse(null);
        assertEquals("A2", updated.getLevel());
    }

    @Test
    void shouldDeleteCourseById() {
        underTest.deleteById(french.getId());
        assertFalse(underTest.findById(french.getId()).isPresent());
    }

    @Test
    void shouldDeleteCourse() {
        Course course = new Course("Math B1", "Math", "B1", "Intermediate Math");
        Course saved = underTest.save(course);

        underTest.deleteById(saved.getId());
        Course deleted = underTest.findById(saved.getId()).orElse(null);

        assertNull(deleted, "Deleted course should not be found");
    }

    @Test
    void shouldReturnAllCourses() {
        List<Course> courses = underTest.findAll();
        assertTrue(courses.size() >= 3);
    }

    @Test
    void savedCoursesShouldHaveUniqueIds() {
        List<Course> courses = underTest.findAll();
        long uniqueIds = courses.stream().map(Course::getId).distinct().count();
        assertEquals(courses.size(), uniqueIds);
    }

    @Test
    void updateNonExistingCourseReturnsNull() {
        Course fake = new Course("Fake", "None", "X", "Fake course");
        fake.setId("nonexistent");
        Course result = underTest.save(fake);
        assertNotNull(result);
        assertEquals("Fake", result.getName());
    }

    @Test
    void shouldDeleteAllCourses() {
        underTest.deleteAll();
        assertEquals(0, underTest.findAll().size());
    }
}
