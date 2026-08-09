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
import java.util.Objects;

/**
 * ConversationMemberEntity — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Entity
@Table(name = "conversation_members")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConversationMemberEntity {

    @EmbeddedId
    private ConversationMemberId id;

    @Column(name = "joined_at", nullable = false)
    Instant joinedAt;
    @Column(name = "role", nullable = false)
    String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationMemberEntity that = (ConversationMemberEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
