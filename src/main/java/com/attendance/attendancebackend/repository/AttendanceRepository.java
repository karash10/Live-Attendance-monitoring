package com.attendance.attendancebackend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.attendance.attendancebackend.model.entity.Attendance;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    // All attendance records for a student
    List<Attendance> findByStudentId(String studentId);

    // Attendance of a student for a specific class
    List<Attendance> findByStudentIdAndClassId(String studentId, String classId);

    // Attendance for a class on a specific date
    List<Attendance> findByClassIdAndDate(String classId, LocalDate date);

    // Single attendance record (unique per student per class per day)
    Optional<Attendance> findByStudentIdAndClassIdAndDate(
            String studentId,
            String classId,
            LocalDate date
    );
}
