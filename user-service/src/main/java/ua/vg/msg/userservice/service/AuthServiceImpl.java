package ua.vg.msg.userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.vg.msg.shared.properties.CommonProperties;
import ua.vg.msg.userservice.dto.auth.LoginRequest;
import ua.vg.msg.userservice.dto.auth.LoginResponse;
import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;
import ua.vg.msg.userservice.dto.auth.RefreshTokenResponse;
import ua.vg.msg.userservice.repository.RefreshTokenRepository;
import ua.vg.msg.userservice.repository.UserRepository;
import ua.vg.msg.userservice.repository.entity.RefreshTokenEntity;
import ua.vg.msg.userservice.service.exception.InvalidCredentialsException;
import ua.vg.msg.userservice.service.exception.InvalidRefreshTokenException;
import ua.vg.msg.shared.accesstokenprovider.AccessTokenProvider;
import ua.vg.msg.userservice.service.tokenprovider.RefreshTokenProvider;

import java.time.LocalDateTime;

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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonProperties commonProperties;
    private final AccessTokenProvider accessTokenProvider;

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid email or password");

        LocalDateTime now = LocalDateTime.now();
        String rawRefreshToken = refreshTokenProvider.generateRefreshToken();
        String refreshTokenHash = refreshTokenProvider.hash(rawRefreshToken);
        LocalDateTime expiration = now.plusDays(commonProperties.getRefreshTokenTtlDays());

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserId(user.getId());
        refreshTokenEntity.setTokenHash(refreshTokenHash);
        refreshTokenEntity.setExpiresAt(expiration);
        refreshTokenEntity.setRevokedAt(null);

        refreshTokenRepository.save(refreshTokenEntity);

        String accessToken = accessTokenProvider.generateAccessToken(user.getId(), user.getUserType().name());
        LocalDateTime accessExpiresAt = accessTokenProvider.extractExpiresAt(accessToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(rawRefreshToken)
                .expiresAt(accessExpiresAt)
                .build();
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

        var user = userRepository.findById(refreshTokenEntity.getUserId())
                .orElseThrow(() -> new InvalidRefreshTokenException("User not found with id"));

        refreshTokenEntity.setRevokedAt(now);
        refreshTokenRepository.save(refreshTokenEntity);

        var newRefreshToken = refreshTokenProvider.generateRefreshToken();
        LocalDateTime expiresAt = now.plusDays(commonProperties.getRefreshTokenTtlDays());

        RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
        newRefreshTokenEntity.setTokenHash(refreshTokenProvider.hash(newRefreshToken));
        newRefreshTokenEntity.setUserId(refreshTokenEntity.getUserId());
        newRefreshTokenEntity.setExpiresAt(expiresAt);
        refreshTokenRepository.save(newRefreshTokenEntity);

        String accessToken = accessTokenProvider.generateAccessToken(refreshTokenEntity.getUserId(), user.getUserType().name());
        LocalDateTime accessExpiresAt = accessTokenProvider.extractExpiresAt(accessToken);

        return RefreshTokenResponse.builder()
                .refreshToken(newRefreshToken)
                .accessToken(accessToken)
                .expiresAt(accessExpiresAt)
                .build();
    }
}
