package com.attendance.attendancebackend.model.session;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveAttendanceSession {

    private final String classId;
    private final String teacherId;
    private final Instant startedAt;

    // studentId -> present / absent
    private final Map<String, Boolean> attendanceMap = new ConcurrentHashMap<>();

    public ActiveAttendanceSession(String classId, String teacherId) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.startedAt = Instant.now();
    }

    public String getClassId() {
        return classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Map<String, Boolean> getAttendanceMap() {
        return attendanceMap;
    }

    public void markAttendance(String studentId, boolean present) {
        attendanceMap.put(studentId, present);
        System.out.println("Marked attendance: " + studentId + " = " + present);
    }
}
