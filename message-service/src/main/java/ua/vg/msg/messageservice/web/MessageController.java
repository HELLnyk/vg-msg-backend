package ua.vg.msg.messageservice.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.vg.msg.messageservice.service.MessageService;
import ua.vg.msg.shared.contract.messaging.v1.api.CreateConversationRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.CreateConversationResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.GetConversationMessagesResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageResponse;

import java.util.UUID;

/**
 * MessageController — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/conversations")
    public ResponseEntity<CreateConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request
    ) {

        return ResponseEntity.ok(messageService.createConversation(request, getUserId()));
    }

    @PostMapping("/messages")
    public ResponseEntity<PostMessageResponse> postMessage(
            @Valid @RequestBody PostMessageRequest request
    ) {

        return ResponseEntity.ok(messageService.postMessage(request, getUserId()));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<GetConversationMessagesResponse> getConversationMessages(
            @PathVariable UUID conversationId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return ResponseEntity.ok(messageService.getConversationMessages(conversationId, cursor, limit, getUserId()));
    }

    private UUID getUserId() {
        return (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
