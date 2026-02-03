package com.attendance.attendancebackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;
import com.attendance.attendancebackend.model.session.ActiveAttendanceSession;
import com.attendance.attendancebackend.service.AttendanceSessionManager;

@RestController
@RequestMapping("/api/attendance/session")
public class AttendanceSessionController {

    private final AttendanceSessionManager sessionManager;

    public AttendanceSessionController(AttendanceSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    // ----------------------------------------
    // Teacher: Start attendance session
    // ----------------------------------------
    @PostMapping("/start/{classId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<ActiveAttendanceSession> startSession(
            @PathVariable String classId,
            Authentication authentication
    ) {
        String teacherId =  authentication.getName();

        ActiveAttendanceSession session =
                sessionManager.startSession(classId, teacherId);

        return new ApiResponse<>(
                true,
                "Attendance session started",
                session
        );
    }

    // ----------------------------------------
    // Check if a session is active
    // ----------------------------------------
    @GetMapping("/active")
    public ApiResponse<Boolean> isSessionActive() {
        return new ApiResponse<>(
                true,
                "Session status",
                sessionManager.hasActiveSession()
        );
    }
}
