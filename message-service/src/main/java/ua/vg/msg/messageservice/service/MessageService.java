package ua.vg.msg.messageservice.service;

import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageResponse;

import java.util.UUID;

public interface MessageService {

    PostMessageResponse postMessage(PostMessageRequest request, UUID actorUserId);
}
