package ua.vg.msg.messageservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * ConversationEntity — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Entity
@Table(name = "conversations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "conversation_type", nullable = false)
    String type;
    @Column(name = "created_at", nullable = false)
    Instant createdAt;
    @Column(name = "created_by", nullable = false)
    UUID createdBy;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
