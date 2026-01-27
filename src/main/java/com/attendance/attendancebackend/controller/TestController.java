package com.attendance.attendancebackend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/secure")
    public ApiResponse<String> secure(Authentication authentication) {
        return new ApiResponse<>(
                true,
                "Access granted",
                "User ID: " + authentication.getPrincipal()
        );
    }
}
