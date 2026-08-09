package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.ConversationEntity;

import java.util.UUID;

@Repository
public interface ConversationEntityRepository  extends JpaRepository<ConversationEntity, UUID> {
}
