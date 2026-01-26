package com.attendance.attendancebackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;
import com.attendance.attendancebackend.model.dto.LoginRequest;
import com.attendance.attendancebackend.model.dto.LoginResponse;
import com.attendance.attendancebackend.model.dto.SignupRequest;
import com.attendance.attendancebackend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ApiResponse<String> signup(@Valid @RequestBody SignupRequest request) {

        authService.signup(request);

        return new ApiResponse<>(
                true,
                "Signup successful",
                "User registered successfully"
        );
    }
    @PostMapping("/login")
public ApiResponse<LoginResponse> login(
        @Valid @RequestBody LoginRequest request) {

    LoginResponse response = authService.login(request);

    return new ApiResponse<>(
            true,
            "Login successful",
            response
    );
}

}
