package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * PostMessageResponse — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Value
public class PostMessageResponse {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version = 1;
    UUID messageId;
    Instant createdAt;
    String status = "sent";
}
