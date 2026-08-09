package ua.vg.msg.shared.contract.messaging.v1.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.vg.msg.shared.contract.messaging.v1.api.AttachmentDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * WsMessageCreatedPayload — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Data
public class WsMessageCreatedPayload {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version = 1;
    UUID id;
    UUID conversationId;
    UUID senderId;
    String text;
    List<AttachmentDto> attachments;
    Instant createdAt;
}
