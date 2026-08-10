package ua.vg.msg.messageservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.vg.msg.messageservice.repository.ConversationEntityRepository;
import ua.vg.msg.messageservice.repository.ConversationMemberEntityRepository;
import ua.vg.msg.messageservice.repository.MessageEntityRepository;
import ua.vg.msg.messageservice.repository.MessageStatusEntityRepository;
import ua.vg.msg.messageservice.repository.entity.ConversationEntity;
import ua.vg.msg.messageservice.repository.entity.ConversationMemberEntity;
import ua.vg.msg.messageservice.repository.entity.ConversationMemberId;
import ua.vg.msg.messageservice.repository.entity.MessageEntity;
import ua.vg.msg.messageservice.service.exception.ConversationNotFoundException;
import ua.vg.msg.messageservice.service.exception.NotConversationMemberException;
import ua.vg.msg.messageservice.websocket.WebSocketHandler;
import ua.vg.msg.shared.contract.messaging.v1.api.CreateConversationRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.CreateConversationResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.ConversationDto;
import ua.vg.msg.shared.contract.messaging.v1.api.GetConversationMessagesResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.GetConversationsResponse;
import ua.vg.msg.shared.contract.messaging.v1.api.MessageDto;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageRequest;
import ua.vg.msg.shared.contract.messaging.v1.api.PostMessageResponse;
import ua.vg.msg.shared.contract.messaging.v1.ws.WsMessageCreatedPayload;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageServiceImpl — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final ConversationEntityRepository conversationEntityRepository;

    private final MessageEntityRepository messageEntityRepository;

    private final ConversationMemberEntityRepository conversationMemberEntityRepository;

    private final MessageStatusEntityRepository messageStatusEntityRepository;

    private final WebSocketHandler webSocketHandler;

    @Override
    public CreateConversationResponse createConversation(CreateConversationRequest request, UUID actorUserId) {
        UUID conversationId = UUID.randomUUID();
        ConversationEntity conversation = new ConversationEntity();
        conversation.setId(conversationId);
        conversation.setType("ONE_TO_ONE");
        conversation.setCreatedAt(Instant.now());
        conversation.setCreatedBy(actorUserId);
        conversationEntityRepository.save(conversation);

        for (UUID memberId : request.getMemberIds()) {
            ConversationMemberEntity member = new ConversationMemberEntity();
            member.setId(new ConversationMemberId(conversationId, memberId));
            member.setRole("MEMBER");
            member.setJoinedAt(Instant.now());
            conversationMemberEntityRepository.save(member);
        }

        return new CreateConversationResponse(conversationId);
    }

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

        MessageEntity saved = messageEntityRepository.save(newMessage);
        
        try {
            WsMessageCreatedPayload payload = new WsMessageCreatedPayload();
            payload.setId(saved.getId());
            payload.setConversationId(saved.getConversationId());
            payload.setSenderId(saved.getSenderId());
            payload.setText(saved.getText());
            payload.setCreatedAt(saved.getCreatedAt());
            
            List<UUID> conversationMembers = conversationMemberEntityRepository.findAllUserIdByConversationId(request.getConversationId());
            webSocketHandler.broadcastMessageToUsers(conversationMembers, payload);
        } catch (Exception e) {
            log.warn("Failed to broadcast message via WebSocket", e);
        }
        
        return PostMessageResponse.builder()
                .messageId(saved.getId())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public GetConversationMessagesResponse getConversationMessages(UUID conversationId, String cursor, int limit, UUID actorUserId) {
        if (!conversationEntityRepository.existsById(conversationId))
            throw new ConversationNotFoundException("Conversation not found: " + conversationId);

        if (!conversationMemberEntityRepository.existsByIdConversationIdAndIdUserId(conversationId, actorUserId))
            throw new NotConversationMemberException("User is not a member of the conversation: " + conversationId);

        List<MessageEntity> messages;
        if (cursor == null || cursor.isEmpty()) {
            messages = messageEntityRepository.findByConversationIdOrderByCreatedAtDescIdDesc(conversationId, limit + 1);
        } else {
            Long cursorSequence = Long.parseLong(new String(Base64.getDecoder().decode(cursor)));
            messages = messageEntityRepository.findByConversationIdAndCursorOrderByCreatedAtDescIdDesc(conversationId, cursorSequence, limit + 1);
        }

        String nextCursor = null;
        boolean hasMore = false;

        if (messages.size() > limit) {
            hasMore = true;
            messages = messages.subList(0, limit);
            MessageEntity lastMessage = messages.get(limit - 1);
            nextCursor = Base64.getEncoder().encodeToString(lastMessage.getSequence().toString().getBytes());
        }

        List<MessageDto> dtos = messages.stream()
                .map(m -> new MessageDto(m.getId(), m.getSenderId(), m.getText(), m.getCreatedAt(), "sent"))
                .toList();

        return new GetConversationMessagesResponse(dtos, nextCursor, hasMore);
    }

    @Override
    public GetConversationsResponse getConversations(UUID userId) {
        List<ConversationEntity> conversations = conversationEntityRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        
        List<ConversationDto> dtos = conversations.stream()
                .map(c -> new ConversationDto(c.getId(), c.getType(), c.getCreatedAt()))
                .toList();
        
        return new GetConversationsResponse(dtos);
    }
}