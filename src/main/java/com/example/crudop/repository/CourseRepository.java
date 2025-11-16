package com.example.crudop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.crudop.model.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
}
