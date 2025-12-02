package com.example.crudop;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.crudop.model.Course;
import com.example.crudop.repository.CourseRepository;
import com.example.crudop.service.CourseService;

@ExtendWith(MockitoExtension.class)
public class CourseMockTest {

    @Mock
    private CourseRepository mockRepository;

    @InjectMocks
    private CourseService underTest;

    @Test
    @DisplayName("Create course: id exists → FAIL")
    void whenCreateCourseAndIdExistsThenFail() {

        Course course = new Course("English A1", "English", "A1", "desc");
        course.setId("123");

        given(mockRepository.existsById("123")).willReturn(true);

        Course persisted = underTest.create(course);

        assertNull(persisted);
        then(mockRepository).should(never()).save(course);
        then(mockRepository).should(times(1)).existsById("123");
    }

    @Test
    @DisplayName("Create course: id not exists → OK")
    void whenCreateCourseAndIdNotExistsThenOk() {

        Course course = new Course("English A1", "English", "A1", "desc");
        course.setId("123");

        given(mockRepository.existsById("123")).willReturn(false);
        given(mockRepository.save(course)).willReturn(course);

        Course persisted = underTest.create(course);

        assertNotNull(persisted);
        assertEquals(course, persisted);
        then(mockRepository).should(times(1)).save(course);
    }

    @Test
    @DisplayName("Update course: id exists → OK")
    void whenUpdateCourseAndIdExistsThenOk() {

        Course course = new Course("English A1", "English", "A1", "desc");
        course.setId("123");

        given(mockRepository.existsById("123")).willReturn(true);
        given(mockRepository.save(course)).willReturn(course);

        Course updated = underTest.update(course);

        assertNotNull(updated);
        assertEquals(course, updated);
        then(mockRepository).should(times(1)).existsById("123");
        then(mockRepository).should(times(1)).save(course);
    }

    @Test
    @DisplayName("Update course: id not exists → FAIL")
    void whenUpdateCourseAndIdNotExistsThenFail() {

        Course course = new Course("English A1", "English", "A1", "desc");
        course.setId("999");

        given(mockRepository.existsById("999")).willReturn(false);

        Course updated = underTest.update(course);

        assertNull(updated);
        then(mockRepository).should(times(1)).existsById("999");
        then(mockRepository).should(never()).save(course);
    }

    @Test
    @DisplayName("Update course: id is null → FAIL")
    void whenUpdateCourseAndIdIsNullThenFail() {

        Course course = new Course();

        Course updated = underTest.update(course);

        assertNull(updated);
        then(mockRepository).should(never()).existsById(any());
        then(mockRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("Get all courses → returns list")
    void whenGetAllThenReturnList() {

        List<Course> mockList = Arrays.asList(
                new Course("English A1", "English", "A1", "desc"),
                new Course("French B1", "French", "B1", "desc2")
        );

        given(mockRepository.findAll()).willReturn(mockList);

        List<Course> result = underTest.getAll();

        assertThat(result).hasSize(2);
        assertEquals(mockList, result);
        then(mockRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("Get course by ID: exists → OK")
    void whenGetByIdExistsThenOk() {

        Course mockCourse = new Course("English A1", "English", "A1", "desc");
        mockCourse.setId("123");

        given(mockRepository.findById("123")).willReturn(Optional.of(mockCourse));

        Course result = underTest.getById("123");

        assertNotNull(result);
        assertEquals(mockCourse, result);
        then(mockRepository).should(times(1)).findById("123");
    }

    @Test
    @DisplayName("Get course by ID: not found → FAIL")
    void whenGetByIdNotExistsThenReturnNull() {

        given(mockRepository.findById("777")).willReturn(Optional.empty());

        Course result = underTest.getById("777");

        assertNull(result);
        then(mockRepository).should(times(1)).findById("777");
    }

    @Test
    @DisplayName("Delete course by ID → OK")
    void whenDeleteThenOk() {

        String id = "123";

        doNothing().when(mockRepository).deleteById(id);

        underTest.delete(id);

        then(mockRepository).should(times(1)).deleteById(id);
    }
}
