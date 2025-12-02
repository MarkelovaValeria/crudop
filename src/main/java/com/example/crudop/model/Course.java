package com.example.crudop.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Document(collection = "courses")
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    private String id;
    private String name;
    private String language;
    private String level;
    private String description;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Course(String name, String language, String level, String description) {
        this.name = name;
        this.language = language;
        this.level = level;
        this.description = description;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Course course)) {
            return false;
        }
        return getId().equals(course.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
