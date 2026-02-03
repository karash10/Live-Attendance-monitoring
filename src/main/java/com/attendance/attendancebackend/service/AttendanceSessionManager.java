package com.attendance.attendancebackend.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.attendance.attendancebackend.model.session.ActiveAttendanceSession;

@Component
public class AttendanceSessionManager {

    private ActiveAttendanceSession activeSession;

    // -----------------------------
    // Start a new session
    // -----------------------------
    public synchronized ActiveAttendanceSession startSession(
            String classId,
            String teacherId
    ) {
        if (activeSession != null) {
            throw new IllegalStateException("An attendance session is already active");
        }

        activeSession = new ActiveAttendanceSession(classId, teacherId);
        return activeSession;
    }

    // -----------------------------
    // Get active session
    // -----------------------------
    public Optional<ActiveAttendanceSession> getActiveSession() {
        return Optional.ofNullable(activeSession);
    }

    // -----------------------------
    // End session
    // -----------------------------
    public synchronized void clearSession() {
        activeSession = null;
    }

    // -----------------------------
    // Check if session active
    // -----------------------------
    public boolean hasActiveSession() {
        return activeSession != null;
    }
}
    