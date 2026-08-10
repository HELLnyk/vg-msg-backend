package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

/**
 * CreateConversationResponse — Response with created conversation ID.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Value
public class CreateConversationResponse {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version;

    UUID conversationId;

    @JsonCreator
    public CreateConversationResponse(
            @JsonProperty("conversationId") UUID conversationId
    ) {
        this.version = 1;
        this.conversationId = conversationId;
    }
}
