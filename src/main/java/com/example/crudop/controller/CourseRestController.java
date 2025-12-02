package com.example.crudop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudop.model.Course;
import com.example.crudop.request.CourseCreateRequest;
import com.example.crudop.request.CourseUpdateRequest;
import com.example.crudop.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/courses/")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService service;

    @RequestMapping("hello")
    public String hello() {
        return "hello from Course REST controller 📚";
    }

    @GetMapping("")
    public List<Course> getAllCourses() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public Course getOne(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return service.create(course);
    }

    @PutMapping
    public Course update(@RequestBody Course course) {
        return service.update(course);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    // DTO
    @PostMapping("/dto")
    public Course insert(@RequestBody CourseCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/dto")
    public Course update(@RequestBody CourseUpdateRequest request) {
        Course existing = service.getById(request.id());
        if (existing == null) {
            return null;
        }

        existing.setName(request.name());
        existing.setLanguage(request.language());
        existing.setLevel(request.level());
        existing.setDescription(request.description());
        existing.setLastModifiedDate(java.time.LocalDateTime.now());

        return service.update(existing);
    }
}
