package ua.vg.msg.messageservice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketHandler — Manages WebSocket sessions and broadcasts messages.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = (UUID) session.getAttributes().get("userId");
        sessions.put(userId, session);
        log.info("User {} connected via WebSocket", userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        UUID userId = (UUID) session.getAttributes().get("userId");
        log.debug("Received message from user {}: {}", userId, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        UUID userId = (UUID) session.getAttributes().get("userId");
        sessions.remove(userId);
        log.info("User {} disconnected from WebSocket", userId);
    }

    public void broadcastMessageToUsers(List<UUID> users, Object payload) throws IOException {
        String messageJson = objectMapper.writeValueAsString(payload);
        TextMessage textMessage = new TextMessage(messageJson);

        for (UUID userId : users) {
            WebSocketSession session = sessions.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    log.error("Failed to send message to user {}: {}", userId, e.getMessage());
                }
            }
        }
    }
}
