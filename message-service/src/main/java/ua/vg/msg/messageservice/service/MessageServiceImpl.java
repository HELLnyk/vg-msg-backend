package ua.vg.msg.messageservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.vg.msg.messageservice.repository.ConversationEntityRepository;
import ua.vg.msg.messageservice.repository.ConversationMemberEntityRepository;
import ua.vg.msg.messageservice.repository.MessageEntityRepository;
import ua.vg.msg.messageservice.repository.MessageStatusEntityRepository;
import ua.vg.msg.messageservice.repository.entity.MessageEntity;
import ua.vg.msg.messageservice.service.exception.ConversationNotFoundException;
import ua.vg.msg.messageservice.service.exception.NotConversationMemberException;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageResponse;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageServiceImpl — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final ConversationEntityRepository conversationEntityRepository;

    private final MessageEntityRepository messageEntityRepository;

    private final ConversationMemberEntityRepository conversationMemberEntityRepository;

    private final MessageStatusEntityRepository messageStatusEntityRepository;


    @Override
    public PostMessageResponse postMessage(PostMessageRequest request, UUID actorUserId) {
        if (!conversationEntityRepository.existsById(request.getConversationId()))
            throw new ConversationNotFoundException("Conversation not found" + request.getConversationId());

        if (!conversationMemberEntityRepository.existsByIdConversationIdAndIdUserId(
                request.getConversationId(),
                actorUserId
        ))
            throw new NotConversationMemberException("User is not a member of the conversation" + request.getConversationId());

        Optional<MessageEntity> optionalMessageEntity = messageEntityRepository.findBySenderIdAndClientMessageId(
                actorUserId,
                request.getClientMessageId()
        );

        if (optionalMessageEntity.isPresent()) {
            MessageEntity entity = optionalMessageEntity.get();
            return PostMessageResponse.builder()
                    .messageId(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }

        MessageEntity newMessage = new MessageEntity();
        newMessage.setId(UUID.randomUUID());
        newMessage.setConversationId(request.getConversationId());
        newMessage.setSenderId(actorUserId);
        newMessage.setClientMessageId(request.getClientMessageId());
        newMessage.setText(request.getText());
        newMessage.setCreatedAt(Instant.now());

        MessageEntity saved =  messageEntityRepository.save(newMessage);
        return PostMessageResponse.builder()
                .messageId(saved.getId())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
