package ua.vg.msg.messageservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * ConversationMemberId — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ConversationMemberId implements Serializable {
    @Serial
    private static final long serialVersionUID = -5703895514006101124L;

    @Column(name = "conversation_id",  nullable = false)
    UUID conversationId;
    @Column(name = "user_id", nullable = false)
    UUID userId;
}
