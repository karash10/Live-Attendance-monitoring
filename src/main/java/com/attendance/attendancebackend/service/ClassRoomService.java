package com.attendance.attendancebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.attendance.attendancebackend.model.entity.ClassRoom;
import com.attendance.attendancebackend.model.entity.User;
import com.attendance.attendancebackend.repository.ClassRoomRepository;
import com.attendance.attendancebackend.repository.UserRepository;

@Service
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;

    public ClassRoomService(
            ClassRoomRepository classRoomRepository,
            UserRepository userRepository
    ) {
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
    }

    // -------------------------------
    // Create class (TEACHER only)
    // -------------------------------
    public ClassRoom createClass(String teacherId, String name) {
        ClassRoom classRoom = new ClassRoom();
        classRoom.setName(name);
        classRoom.setTeacherId(teacherId);
        return classRoomRepository.save(classRoom);
    }

    // -------------------------------
    // Get classes for teacher
    // -------------------------------
    public List<ClassRoom> getClassesForTeacher(String teacherId) {
        return classRoomRepository.findByTeacherId(teacherId);
    }

    // -------------------------------
    // Get classes for student
    // -------------------------------
    public List<ClassRoom> getClassesForStudent(String studentId) {
        return classRoomRepository.findByStudentIdsContaining(studentId);
    }

    // -------------------------------
    // Add student to class (TEACHER only)
    // -------------------------------
    public ClassRoom addStudentToClass(
            String classId,
            String teacherId,
            String studentId
    ) {
        // 1. Validate class ownership
        ClassRoom classRoom = classRoomRepository
                .findByIdAndTeacherId(classId, teacherId)
                .orElseThrow(() ->
                        new RuntimeException("Class not found or access denied")
                );

        // 2. Validate student existence
        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found")
                );

        // 3. Validate role
        if (!student.getRole().name().equals("STUDENT")) {
            throw new RuntimeException("User is not a student");
        }

        // 4. Prevent duplicate enrollment
        if (classRoom.getStudentIds().contains(studentId)) {
            throw new RuntimeException("Student already enrolled");
        }

        // 5. Add student and save
        classRoom.getStudentIds().add(studentId);
        return classRoomRepository.save(classRoom);
    }
}
