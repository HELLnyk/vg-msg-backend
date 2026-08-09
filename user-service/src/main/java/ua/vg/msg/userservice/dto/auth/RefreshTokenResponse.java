package ua.vg.msg.userservice.dto.auth;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * RefreshTokenResponse — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Value
@Builder
public class RefreshTokenResponse {
    String accessToken;
    String refreshToken;
    LocalDateTime expiresAt;
}
