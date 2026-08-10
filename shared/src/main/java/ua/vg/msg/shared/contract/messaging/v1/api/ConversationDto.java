package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * ConversationDto — Conversation data transfer object for listing.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Value
public class ConversationDto {
    UUID conversationId;
    String type;
    Instant createdAt;

    @JsonCreator
    public ConversationDto(
            @JsonProperty("conversationId") UUID conversationId,
            @JsonProperty("type") String type,
            @JsonProperty("createdAt") Instant createdAt
    ) {
        this.conversationId = conversationId;
        this.type = type;
        this.createdAt = createdAt;
    }
}
