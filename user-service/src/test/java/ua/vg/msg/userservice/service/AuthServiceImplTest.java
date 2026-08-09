package ua.vg.msg.userservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;
import ua.vg.msg.userservice.repository.RefreshTokenRepository;
import ua.vg.msg.userservice.repository.entity.RefreshTokenEntity;
import ua.vg.msg.userservice.service.exception.InvalidRefreshTokenException;
import ua.vg.msg.userservice.service.tokenprovider.RefreshTokenProviderImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Spy
    private RefreshTokenProviderImpl provider = new  RefreshTokenProviderImpl();

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRefreshTokenSuccessfullyRotatesToken() {
        String oldToken = "old-refresh-token";
        UUID userId = UUID.randomUUID();
        RefreshTokenRequest request = new RefreshTokenRequest(oldToken);

        RefreshTokenEntity activeToken = new RefreshTokenEntity();
        activeToken.setId(1L);
        activeToken.setUserId(userId);
        activeToken.setTokenHash(sha256Hex(oldToken));
        activeToken.setCreatedAt(LocalDateTime.now().minusDays(1));
        activeToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(anyString(), any()))
                .thenReturn(Optional.of(activeToken));
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = authService.refreshToken(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("TODO", response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
        Assertions.assertNotEquals(oldToken, response.getRefreshToken());
        Assertions.assertNotNull(response.getExpiresAt());
        Assertions.assertNotNull(activeToken.getRevokedAt());

        ArgumentCaptor<RefreshTokenEntity> saveCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        verify(refreshTokenRepository, times(2)).save(saveCaptor.capture());
        RefreshTokenEntity newEntity = saveCaptor.getAllValues().get(1);

        Assertions.assertEquals(userId, newEntity.getUserId());
        Assertions.assertEquals(sha256Hex(response.getRefreshToken()), newEntity.getTokenHash());
        Assertions.assertNull(newEntity.getRevokedAt());
    }

    @Test
    void testRefreshTokenThrowsWhenTokenNotFound() {
        RefreshTokenRequest request = new RefreshTokenRequest("missing-token");
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(anyString(), any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                InvalidRefreshTokenException.class,
                () -> authService.refreshToken(request)
        );
    }

    private String sha256Hex(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", e);
        }
    }
}
