package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

import java.util.List;
import java.util.UUID;

/**
 * CreateConversationRequest — Request to create a new conversation.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Value
public class CreateConversationRequest {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version;
    @NotEmpty
    List<UUID> memberIds;

    @JsonCreator
    public CreateConversationRequest(
            @JsonProperty("memberIds") List<UUID> memberIds
    ) {
        this.version = 1;
        this.memberIds = memberIds;
    }
}
