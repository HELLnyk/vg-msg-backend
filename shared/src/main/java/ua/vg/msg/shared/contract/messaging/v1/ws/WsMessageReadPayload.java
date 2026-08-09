package ua.vg.msg.shared.contract.messaging.v1.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * WsMessageReadPayload — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Data
public class WsMessageReadPayload {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version = 1;
    UUID messageId;
    UUID userId;
    Instant readAt;
}
