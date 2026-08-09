package ua.vg.msg.shared.contract.messaging.v1.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * MessageEventEnvelope — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Data
public class MessageEventEnvelope<T>{

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    final int version = 1;

    UUID eventId;
    MessageEventType eventType;
    Instant occurredAt;
    UUID conversationId;
    UUID messageId;
    UUID actorUserId;
    T payload;
}
