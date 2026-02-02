package com.attendance.attendancebackend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.attendance.attendancebackend.model.dto.AttendanceStatusResponse;
import com.attendance.attendancebackend.model.entity.Attendance;
import com.attendance.attendancebackend.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // ----------------------------------------
    // Save attendance for a student (basic)
    // ----------------------------------------
    public Attendance markAttendance(
            String studentId,
            String classId,
            boolean present
    ) {
        LocalDate today = LocalDate.now();

        // Prevent duplicate attendance for same day
        attendanceRepository
                .findByStudentIdAndClassIdAndDate(studentId, classId, today)
                .ifPresent(existing -> {
                    throw new RuntimeException("Attendance already marked for today");
                });

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setClassId(classId);
        attendance.setDate(today);
        attendance.setPresent(present);

        return attendanceRepository.save(attendance);
    }
    public AttendanceStatusResponse getTodayAttendanceStatus(
        String studentId,
        String classId
) {
    LocalDate today = LocalDate.now();

    return attendanceRepository
            .findByStudentIdAndClassIdAndDate(studentId, classId, today)
            .map(attendance -> new AttendanceStatusResponse(
                    classId,
                    today,
                    attendance.isPresent() ? "PRESENT" : "ABSENT"
            ))
            .orElse(
                    new AttendanceStatusResponse(
                            classId,
                            today,
                            "NOT_MARKED"
                    )
            );
}
public List<Attendance> getAttendanceForStudent(String studentId) {
    return attendanceRepository.findByStudentId(studentId);
}

public List<Attendance> getAttendanceForStudentAndClass(String studentId, String classId) {
    return attendanceRepository.findByStudentIdAndClassId(studentId, classId);
}

}
