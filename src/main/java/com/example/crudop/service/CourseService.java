package com.example.crudop.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crudop.model.Course;
import com.example.crudop.repository.CourseRepository;
import com.example.crudop.request.CourseCreateRequest;
import com.example.crudop.request.CourseUpdateRequest;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;

    @PostConstruct
    void init() {
        if (repository.count() == 0) {
            List<Course> courses = Arrays.asList(
                    new Course("English A1", "English", "A1", "Beginner English course"),
                    new Course("French B1", "French", "B1", "Intermediate French course"),
                    new Course("German A2", "German", "A2", "Basic German course"),
                    new Course("Spanish B2", "Spanish", "B2", "Upper-intermediate Spanish course"),
                    new Course("Italian C1", "Italian", "C1", "Advanced Italian course")
            );
            repository.saveAll(courses);
        }
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public Course getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Course create(Course course) {
        if (course.getId() != null && repository.existsById(course.getId())) {
            return null;
        }
        course.setCreatedDate(LocalDateTime.now());
        course.setLastModifiedDate(null);
        return repository.save(course);
    }

    public Course create(CourseCreateRequest course) {
        Course created = mapToCourse(course);
        created.setCreatedDate(LocalDateTime.now());
        created.setLastModifiedDate(null);

        if (created.getId() != null && repository.existsById(created.getId())) {
            return null;
        }

        return repository.save(created);
    }

    public Course update(Course course) {
        if (course == null || course.getId() == null) {
            return null;
        }

        if (!repository.existsById(course.getId())) {
            return null;
        }

        course.setLastModifiedDate(LocalDateTime.now());
        return repository.save(course);
    }

    public Course update(CourseUpdateRequest request) {
        Course existing = repository.findById(request.id()).orElse(null);

        if (existing == null) {
            return null;
        }

        Course updated = new Course(
                request.name(),
                request.language(),
                request.level(),
                request.description()
        );
        updated.setId(existing.getId());
        updated.setCreatedDate(existing.getCreatedDate());
        updated.setLastModifiedDate(LocalDateTime.now());

        return repository.save(updated);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private Course mapToCourse(CourseCreateRequest request) {
        Course item = new Course(request.name(), request.language(), request.level(), request.description());
        return item;
    }
}
