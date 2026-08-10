package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

/**
 * GetConversationMessagesResponse — Paginated response for conversation messages.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@Value
public class GetConversationMessagesResponse {
    List<MessageDto> messages;
    String nextCursor;
    boolean hasMore;

    @JsonCreator
    public GetConversationMessagesResponse(
            @JsonProperty("messages") List<MessageDto> messages,
            @JsonProperty("nextCursor") String nextCursor,
            @JsonProperty("hasMore") boolean hasMore
    ) {
        this.messages = messages;
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
    }
}
