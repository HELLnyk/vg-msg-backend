package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.ConversationMemberEntity;
import ua.vg.msg.messageservice.repository.entity.ConversationMemberId;

import java.util.List;
import java.util.UUID;

/**
 * ConversationMemberEntityRepository — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Repository
public interface ConversationMemberEntityRepository extends JpaRepository<ConversationMemberEntity, ConversationMemberId> {

    boolean existsByIdConversationIdAndIdUserId(UUID conversationId, UUID userId);

    @Query("SELECT m.id.userId FROM ConversationMemberEntity m WHERE m.id.conversationId = :conversationId")
    List<UUID> findAllUserIdByConversationId(UUID conversationId);
}
