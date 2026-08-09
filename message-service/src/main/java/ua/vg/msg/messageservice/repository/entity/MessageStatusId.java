package ua.vg.msg.messageservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * MessageStatusId — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Data
@Embeddable
public class MessageStatusId implements Serializable {
    @Serial
    private static final long serialVersionUID = 8687701073190122587L;

    @Column(name = "message_id", nullable = false)
    private UUID messageId;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "status", nullable = false)
    private String status;
}
