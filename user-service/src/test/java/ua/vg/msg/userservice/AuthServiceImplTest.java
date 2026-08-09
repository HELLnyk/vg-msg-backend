package ua.vg.msg.userservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.vg.msg.userservice.config.CommonProperties;
import ua.vg.msg.userservice.dto.auth.LoginRequest;
import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;
import ua.vg.msg.userservice.repository.RefreshTokenRepository;
import ua.vg.msg.userservice.repository.entity.RefreshTokenEntity;
import ua.vg.msg.userservice.repository.entity.UserEntity;
import ua.vg.msg.userservice.service.AuthServiceImpl;
import ua.vg.msg.userservice.service.UserService;
import ua.vg.msg.userservice.service.exception.InvalidCredentialsException;
import ua.vg.msg.userservice.service.exception.InvalidRefreshTokenException;
import ua.vg.msg.userservice.service.exception.UserNotFoundException;
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

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private RefreshTokenProviderImpl provider = new  RefreshTokenProviderImpl();

    @Spy
    private CommonProperties commonProperties;

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

    @Test
    void testLoginNotFoundUser() {
        LoginRequest request = new LoginRequest("missing-email", "password");
        when(userService.getUserByEmail(request.getEmail())).thenThrow(new UserNotFoundException("User not found"));

        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> authService.login(request)
        );
    }

    @Test
    void testBadCredentials() {
        LoginRequest request = new LoginRequest("foo@bar.com", "password");
        UserEntity user = new UserEntity();
        user.setEmail("foo@bar.com");
        user.setPassword("password");

        when(userService.getUserByEmail(request.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Assertions.assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));

        verify(userService, times(1)).getUserByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void testLoginSuccessfully() {
        String email = "foo@bar.com";
        String rawPassword = "plain-password";
        String encodedPassword = "encoded-password";
        UUID userId = UUID.randomUUID();

        LoginRequest request = new LoginRequest(email, rawPassword);
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        commonProperties.setRefreshTokenTtlDays(5);

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = authService.login(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("TODO", response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
        Assertions.assertFalse(response.getRefreshToken().isBlank());
        Assertions.assertNotNull(response.getExpiresAt());

        ArgumentCaptor<RefreshTokenEntity> saveCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        verify(refreshTokenRepository, times(1)).save(saveCaptor.capture());
        RefreshTokenEntity savedToken = saveCaptor.getValue();

        Assertions.assertEquals(userId, savedToken.getUserId());
        Assertions.assertEquals(sha256Hex(response.getRefreshToken()), savedToken.getTokenHash());
        Assertions.assertNull(savedToken.getRevokedAt());
        Assertions.assertEquals(response.getExpiresAt(), savedToken.getExpiresAt());
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
