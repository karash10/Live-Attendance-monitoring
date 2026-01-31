package com.attendance.attendancebackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateClassRequest {

    @NotBlank
    private String name;
}
