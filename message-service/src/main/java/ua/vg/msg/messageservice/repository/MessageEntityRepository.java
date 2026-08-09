package ua.vg.msg.messageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vg.msg.messageservice.repository.entity.MessageEntity;

import java.util.UUID;

/**
 * MessageEntityRepository — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, UUID> {
}
