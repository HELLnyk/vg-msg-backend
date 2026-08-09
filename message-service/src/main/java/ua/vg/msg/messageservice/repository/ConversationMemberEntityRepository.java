package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.ConversationMemberEntity;
import ua.vg.msg.messageservice.repository.entity.ConversationMemberId;

/**
 * ConversationMemberEntityRepository — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Repository
public interface ConversationMemberEntityRepository extends JpaRepository<ConversationMemberEntity, ConversationMemberId> {
}
