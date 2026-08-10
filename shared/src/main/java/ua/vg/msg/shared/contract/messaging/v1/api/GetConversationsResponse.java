package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

/**
 * GetConversationsResponse — List of user's conversations.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Value
public class GetConversationsResponse {
    List<ConversationDto> conversations;

    @JsonCreator
    public GetConversationsResponse(
            @JsonProperty("conversations") List<ConversationDto> conversations
    ) {
        this.conversations = conversations;
    }
}
