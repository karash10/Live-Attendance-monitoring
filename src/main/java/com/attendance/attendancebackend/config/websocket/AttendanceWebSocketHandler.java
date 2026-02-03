package com.attendance.attendancebackend.config.websocket;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.attendance.attendancebackend.model.dto.WebSocketMessageDto;
import com.attendance.attendancebackend.model.session.ActiveAttendanceSession;
import com.attendance.attendancebackend.model.session.WebSocketSessionInfo;
import com.attendance.attendancebackend.service.AttendanceSessionManager;
import com.attendance.attendancebackend.service.WebSocketSessionRegistry;
import com.attendance.attendancebackend.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Claims;

@Component
public class AttendanceWebSocketHandler implements WebSocketHandler {

    private final JwtUtil jwtUtil;
    private final WebSocketSessionRegistry registry;
    private final AttendanceSessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AttendanceWebSocketHandler(
            JwtUtil jwtUtil,
            WebSocketSessionRegistry registry,
            AttendanceSessionManager sessionManager
    ) {
        this.jwtUtil = jwtUtil;
        this.registry = registry;
        this.sessionManager = sessionManager;
    }

    // -------------------------------
    // On WebSocket connect
    // -------------------------------
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String token = extractToken(session.getUri());
        if (token == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
            return;
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            registry.register(new WebSocketSessionInfo(session, userId, role));
        } catch (Exception e) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
        }
    }

    // -------------------------------
    // Handle incoming messages
    // -------------------------------
@Override
public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    WebSocketSessionInfo info =
            registry.getAllSessions().stream()
                    .filter(s -> s.getSession().getId().equals(session.getId()))
                    .findFirst()
                    .orElse(null);

    if (info == null) return;

    WebSocketMessageDto msg = objectMapper.readValue(
            message.getPayload().toString(),
            WebSocketMessageDto.class
    );

    String type = msg.getType();

    // -------------------------
    // STUDENT: MARK_ATTENDANCE
    // -------------------------
    if ("MARK_ATTENDANCE".equals(type)) {

        if (!"STUDENT".equals(info.getRole())) {
            return;
        }

        ActiveAttendanceSession activeSession =
                sessionManager.getActiveSession().orElse(null);

        if (activeSession == null) return;

        Boolean present = (Boolean) msg.getPayload().get("present");
        if (present == null) return;

        activeSession.markAttendance(info.getUserId(), present);

        System.out.println(
                "[WS] Attendance marked → studentId="
                        + info.getUserId()
                        + ", present="
                        + present
        );

        broadcastAttendanceUpdate(info.getUserId(), present);
    }

    // -------------------------
    // TEACHER: END_SESSION
    // -------------------------
    else if ("END_SESSION".equals(type)) {

        if (!"TEACHER".equals(info.getRole())) {
            return;
        }

        System.out.println("[WS] Attendance session ended by teacher");

        // Broadcast to all clients
        ObjectNode endMsg = objectMapper.createObjectNode();
        endMsg.put("type", "SESSION_ENDED");

        registry.getAllSessions().forEach(ws -> {
            try {
                ws.getSession().sendMessage(
                        new TextMessage(endMsg.toString())
                );
            } catch (Exception ignored) {}
        });

        // Clear in-memory session
        sessionManager.clearSession();
    }
}


    // -------------------------------
    // On transport error
    // -------------------------------
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        registry.remove(session.getId());
    }

    // -------------------------------
    // On connection close
    // -------------------------------
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        registry.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // -------------------------------
    // Helper: extract token from URL
    // -------------------------------
    private String extractToken(URI uri) {
        if (uri == null || uri.getQuery() == null) return null;

        for (String param : uri.getQuery().split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("token")) {
                return pair[1];
            }
        }
        return null;
    }
    private void broadcastAttendanceUpdate(String studentId, boolean present) {

    ObjectNode payload = objectMapper.createObjectNode();
    payload.put("studentId", studentId);
    payload.put("present", present);

    ObjectNode message = objectMapper.createObjectNode();
    message.put("type", "ATTENDANCE_UPDATE");
    message.set("payload", payload);

    registry.getAllSessions().forEach(info -> {
        try {
            info.getSession().sendMessage(
                    new TextMessage(message.toString())
            );
        } catch (Exception e) {
            // Ignore failed sends for now
        }
    });
}

}
