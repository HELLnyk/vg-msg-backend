package ua.vg.msg.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.vg.msg.userservice.repository.entity.RefreshTokenEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(String tokenHash, LocalDateTime expiresAt);
}
