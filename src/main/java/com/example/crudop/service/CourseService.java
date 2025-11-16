package com.example.crudop.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crudop.model.Course;
import com.example.crudop.repository.CourseRepository;

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
        return repository.save(course);
    }

    public Course update(Course course) {
        if (course.getId() == null) return null;
        return repository.save(course);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}