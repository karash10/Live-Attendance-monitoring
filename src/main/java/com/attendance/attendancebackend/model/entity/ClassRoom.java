package com.attendance.attendancebackend.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "classes")
@Data
public class ClassRoom {

    @Id
    private String id;

    private String name;

    // Teacher who owns this class
    private String teacherId;

    // Enrolled students (userIds)
    private List<String> studentIds = new ArrayList<>();
}
