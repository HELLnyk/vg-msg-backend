package ua.vg.msg.userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.vg.msg.userservice.dto.auth.LoginRequest;
import ua.vg.msg.userservice.dto.auth.LoginResponse;
import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;
import ua.vg.msg.userservice.dto.auth.RefreshTokenResponse;
import ua.vg.msg.userservice.repository.RefreshTokenRepository;
import ua.vg.msg.userservice.repository.entity.RefreshTokenEntity;
import ua.vg.msg.userservice.repository.entity.UserEntity;
import ua.vg.msg.userservice.service.exception.InvalidRefreshTokenException;
import ua.vg.msg.userservice.service.tokenprovider.RefreshTokenProvider;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AuthServiceImpl — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        LocalDateTime now = LocalDateTime.now();
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String tokenHash = refreshTokenProvider.hash(refreshToken);

        var refreshTokenEntity = refreshTokenRepository
                .findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(tokenHash, now)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

        refreshTokenEntity.setRevokedAt(now);
        refreshTokenRepository.save(refreshTokenEntity);

        var newRefreshToken = refreshTokenProvider.generateRefreshToken();
        LocalDateTime expiresAt = now.plusDays(5);

        RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
        newRefreshTokenEntity.setTokenHash(refreshTokenProvider.hash(newRefreshToken));
        newRefreshTokenEntity.setUserId(refreshTokenEntity.getUserId());
        newRefreshTokenEntity.setExpiresAt(expiresAt);
        refreshTokenRepository.save(newRefreshTokenEntity);

        return RefreshTokenResponse.builder()
                .refreshToken(newRefreshToken)
                .accessToken("TODO") //would be added after
                .expiresAt(expiresAt)
                .build();
    }


}
