package com.attendance.attendancebackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ApiResponse<String> health() {
        return new ApiResponse<>(true, "Service is up", "Attendance Backend is running");
    }
}
