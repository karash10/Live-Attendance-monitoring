package com.attendance.attendancebackend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendancebackend.model.dto.ApiResponse;
import com.attendance.attendancebackend.model.entity.ClassRoom;
import com.attendance.attendancebackend.service.ClassRoomService;

@RestController
@RequestMapping("/api/classes")
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    public ClassRoomController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    @GetMapping("/teacher")
@PreAuthorize("hasRole('TEACHER')")
public ApiResponse<List<ClassRoom>> getTeacherClasses(Authentication authentication) {

    String teacherId = (String) authentication.getPrincipal();

    return new ApiResponse<>(
            true,
            "Teacher classes fetched",
            classRoomService.getClassesForTeacher(teacherId)
    );
}
@PostMapping("/{classId}/students/{studentId}")
@PreAuthorize("hasRole('TEACHER')")
public ApiResponse<ClassRoom> addStudent(
        @PathVariable String classId,
        @PathVariable String studentId,
        Authentication authentication
) {
    String teacherId = (String) authentication.getPrincipal();

    return new ApiResponse<>(
            true,
            "Student added to class",
            classRoomService.addStudentToClass(classId, teacherId, studentId)
    );
}


    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<ClassRoom>> getStudentClasses(Authentication authentication) {
        String studentId = authentication.getPrincipal().toString();
        return new ApiResponse<>(
                true,
                "Student classes fetched",
                classRoomService.getClassesForStudent(studentId)
        );
    }
}
