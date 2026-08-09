package ua.vg.msg.userservice.service.tokenprovider;

import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;

/**
 * RefreshTokenProvider — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
public interface RefreshTokenProvider {

    String generateRefreshToken();

    String hash(String refreshToken);
}
