package com.attendance.attendancebackend.model.dto;

import java.util.Map;

public class WebSocketMessageDto {

    private String type;
    private Map<String, Object> payload;

    public String getType() {
        return type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
