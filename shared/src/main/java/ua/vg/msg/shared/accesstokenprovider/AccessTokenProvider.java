package ua.vg.msg.shared.accesstokenprovider;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AccessTokenProvider — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
public interface AccessTokenProvider {
    String generateAccessToken(UUID userId, String role);
    UUID extractUserId(String token);
    boolean isValid(String token);
    LocalDateTime extractExpiresAt(String token);
}
