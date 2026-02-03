package com.attendance.attendancebackend.model.session;

import org.springframework.web.socket.WebSocketSession;

public class WebSocketSessionInfo {

    private final WebSocketSession session;
    private final String userId;
    private final String role;

    public WebSocketSessionInfo(WebSocketSession session, String userId, String role) {
        this.session = session;
        this.userId = userId;
        this.role = role;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
