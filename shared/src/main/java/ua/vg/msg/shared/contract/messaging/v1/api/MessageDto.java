package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * MessageDto — Message data transfer object for API responses.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Value
public class MessageDto {
    UUID messageId;
    UUID senderId;
    String text;
    Instant createdAt;
    String status;

    @JsonCreator
    public MessageDto(
            @JsonProperty("messageId") UUID messageId,
            @JsonProperty("senderId") UUID senderId,
            @JsonProperty("text") String text,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("status") String status
    ) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.text = text;
        this.createdAt = createdAt;
        this.status = status;
    }
}
