package com.example.crudop;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudop.model.Course;
import com.example.crudop.service.CourseService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuditTests {

    @Autowired
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course("Test Course", "TestLang", "T1", "###test audit");
        courseService.create(testCourse);
    }

    @AfterEach
    void tearDown() {
        List<Course> toDelete = courseService.getAll().stream()
                .filter(c -> c.getDescription().contains("###test"))
                .toList();
        for (Course c : toDelete) {
            courseService.delete(c.getId());
        }
    }

    @Test
    void whenCreateCourseThenAuditFieldsAreSet() {
        Course created = courseService.create(new Course("Audit Test", "English", "A1", "###test audit"));

        assertNotNull(created.getId(), "Id має бути присвоєний");
        assertNotNull(created.getCreatedDate(), "CreatedDate має бути встановлено");
        assertNotNull(created.getLastModifiedDate(), "LastModifiedDate має бути встановлено");
        assertEquals(created.getCreatedDate(), created.getLastModifiedDate(), "При створенні CreatedDate == LastModifiedDate");
    }

    @Test
    void whenUpdateCourseThenLastModifiedDateChanges() throws InterruptedException {
        Course created = courseService.create(new Course("Update Test", "French", "B1", "###test audit"));

        Thread.sleep(1000);
        created.setLevel("B2");
        Course updated = courseService.update(created);

        assertNotNull(updated.getLastModifiedDate());
        assertTrue(updated.getLastModifiedDate().isAfter(updated.getCreatedDate()), "LastModifiedDate має бути пізніше CreatedDate після оновлення");
    }

    @Test
    void getAllCoursesContainsTestCourse() {
        List<Course> courses = courseService.getAll();
        assertTrue(courses.stream().anyMatch(c -> c.getDescription().contains("###test")), "Список курсів має містити тестовий курс");
    }

    @Test
    void whenDeleteCourseThenItIsRemoved() {
        Course created = courseService.create(new Course("Delete Test", "German", "A2", "###test audit"));
        courseService.delete(created.getId());

        Course found = courseService.getById(created.getId());
        assertNull(found, "Після видалення курс не має бути в БД");
    }

    @Test
    void whenCreateCourseThenAuditFieldsAreCorrect() {
        Course course = new Course("Audit New Test", "Spanish", "B2", "###test audit");

        Course created = courseService.create(course);

        assertNotNull(created.getId(), "Id має бути присвоєний");
        assertNotNull(created.getCreatedDate(), "CreatedDate має бути встановлено");
        assertNotNull(created.getLastModifiedDate(), "LastModifiedDate має бути встановлено");
        assertTrue(created.getLastModifiedDate().isEqual(created.getCreatedDate())
                || created.getLastModifiedDate().isAfter(created.getCreatedDate()),
                "LastModifiedDate має бути не раніше CreatedDate");
    }

}
