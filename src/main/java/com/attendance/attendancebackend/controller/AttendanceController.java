package com.attendance.attendancebackend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;
import com.attendance.attendancebackend.model.dto.AttendanceStatusResponse;
import com.attendance.attendancebackend.model.entity.Attendance;
import com.attendance.attendancebackend.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // ----------------------------------------
    // Student: Get my attendance (all records)
    // ----------------------------------------
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<Attendance>> getMyAttendance(Authentication authentication) {

        String studentId = (String) authentication.getPrincipal();

        return new ApiResponse<>(
                true,
                "Attendance fetched",
                attendanceService.getAttendanceForStudent(studentId)
        );
    }

    // ----------------------------------------
    // Student: Get my attendance for a class
    // ----------------------------------------
    @GetMapping("/me/{classId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<Attendance>> getMyAttendanceForClass(
            @PathVariable String classId,
            Authentication authentication
    ) {
        String studentId = (String) authentication.getPrincipal();

        return new ApiResponse<>(
                true,
                "Attendance fetched",
                attendanceService.getAttendanceForStudentAndClass(studentId, classId)
        );
    }

    // ----------------------------------------
    // Student: Get today attendance status
    // ----------------------------------------
    @GetMapping("/me/{classId}/today")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<AttendanceStatusResponse> getTodayAttendance(
            @PathVariable String classId,
            Authentication authentication
    ) {
        String studentId = (String) authentication.getPrincipal();

        return new ApiResponse<>(
                true,
                "Today attendance status",
                attendanceService.getTodayAttendanceStatus(studentId, classId)
        );
    }
}
