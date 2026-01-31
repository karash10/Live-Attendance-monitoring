package com.attendance.attendancebackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.attendance.attendancebackend.model.entity.ClassRoom;

public interface ClassRoomRepository extends MongoRepository<ClassRoom, String> {

    List<ClassRoom> findByTeacherId(String teacherId);

    List<ClassRoom> findByStudentIdsContaining(String studentId);

    Optional<ClassRoom> findByIdAndTeacherId(String id, String teacherId);
}
