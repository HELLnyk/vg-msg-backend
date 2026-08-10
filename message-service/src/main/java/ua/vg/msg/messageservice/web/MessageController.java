package ua.vg.msg.messageservice.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vg.msg.messageservice.service.MessageService;
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
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<PostMessageResponse> postMessage(@Valid @RequestBody PostMessageRequest request) {

        UUID userId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(messageService.postMessage(request, userId));
    }

}
