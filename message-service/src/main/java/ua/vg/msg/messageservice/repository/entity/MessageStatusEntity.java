package ua.vg.msg.messageservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * MessageStatusEntity — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Entity
@Table(name = "message_statuses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageStatusEntity {
    @EmbeddedId
    private MessageStatusId messageStatusId;
    @Column(name = "status_at", nullable = false)
    Instant statusAt;
}
