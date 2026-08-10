package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.ConversationEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationEntityRepository extends JpaRepository<ConversationEntity, UUID> {

    @Query("SELECT c FROM ConversationEntity c JOIN ConversationMemberEntity m ON c.id = m.id.conversationId WHERE m.id.userId = :userId ORDER BY c.createdAt DESC")
    List<ConversationEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
