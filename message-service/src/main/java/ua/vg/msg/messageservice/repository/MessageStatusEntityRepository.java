package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.MessageStatusEntity;
import ua.vg.msg.messageservice.repository.entity.MessageStatusId;

/**
 * MessageStatusEntityRepository — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Repository
public interface MessageStatusEntityRepository extends JpaRepository<MessageStatusEntity, MessageStatusId> {
}
