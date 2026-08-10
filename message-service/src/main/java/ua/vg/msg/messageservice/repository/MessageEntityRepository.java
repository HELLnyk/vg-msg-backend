package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.MessageEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageEntityRepository — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, UUID> {

    Optional<MessageEntity> findBySenderIdAndClientMessageId(UUID senderId, UUID clientMessageId);

    @Query("SELECT m FROM MessageEntity m WHERE m.conversationId = :conversationId ORDER BY m.sequence DESC LIMIT :limit")
    List<MessageEntity> findByConversationIdOrderByCreatedAtDescIdDesc(UUID conversationId, int limit);

    @Query("SELECT m FROM MessageEntity m WHERE m.conversationId = :conversationId AND m.sequence < :cursorSequence ORDER BY m.sequence DESC LIMIT :limit")
    List<MessageEntity> findByConversationIdAndCursorOrderByCreatedAtDescIdDesc(UUID conversationId, Long cursorSequence, int limit);
}
