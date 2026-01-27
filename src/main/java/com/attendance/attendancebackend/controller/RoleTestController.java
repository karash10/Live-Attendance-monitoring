package com.attendance.attendancebackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;

@RestController
@RequestMapping("/api/role-test")
public class RoleTestController {

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<String> teacherOnly() {
        return new ApiResponse<>(
                true,
                "Teacher access granted",
                "Only teachers can see this"
        );
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<String> studentOnly() {
        return new ApiResponse<>(
                true,
                "Student access granted",
                "Only students can see this"
        );
    }
}
