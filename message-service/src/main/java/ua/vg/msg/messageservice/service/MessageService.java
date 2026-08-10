package ua.vg.msg.messageservice.service;

import ua.vg.msg.shared.contract.messaging.v1.api.CreateConversationRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.CreateConversationResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.GetConversationMessagesResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageResponse;

import java.util.UUID;

public interface MessageService {

    CreateConversationResponse createConversation(CreateConversationRequest request, UUID actorUserId);

    PostMessageResponse postMessage(PostMessageRequest request, UUID actorUserId);

    GetConversationMessagesResponse getConversationMessages(UUID conversationId, String cursor, int limit, UUID actorUserId);
}
