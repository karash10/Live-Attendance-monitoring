package com.attendance.attendancebackend.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.attendance.attendancebackend.model.session.WebSocketSessionInfo;

@Component
public class WebSocketSessionRegistry {

    // sessionId -> session info
    private final Map<String, WebSocketSessionInfo> sessions = new ConcurrentHashMap<>();

    public void register(WebSocketSessionInfo info) {
        sessions.put(info.getSession().getId(), info);
    }

    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    public Collection<WebSocketSessionInfo> getAllSessions() {
        return sessions.values();
    }

    public Collection<WebSocketSessionInfo> getStudents() {
        return sessions.values().stream()
                .filter(s -> "STUDENT".equals(s.getRole()))
                .toList();
    }

    public Collection<WebSocketSessionInfo> getTeachers() {
        return sessions.values().stream()
                .filter(s -> "TEACHER".equals(s.getRole()))
                .toList();
    }
}
