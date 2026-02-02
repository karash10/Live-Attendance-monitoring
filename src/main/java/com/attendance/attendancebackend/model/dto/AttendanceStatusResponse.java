package com.attendance.attendancebackend.model.dto;

import java.time.LocalDate;

public class AttendanceStatusResponse {

    private String classId;
    private LocalDate date;
    private String status; // PRESENT, ABSENT, NOT_MARKED

    public AttendanceStatusResponse(String classId, LocalDate date, String status) {
        this.classId = classId;
        this.date = date;
        this.status = status;
    }

    public String getClassId() {
        return classId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
