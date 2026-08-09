package ua.vg.msg.userservice.service.tokenprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.vg.msg.userservice.config.CommonProperties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

/**
 * AccessTokenProviderImpl — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Service
@RequiredArgsConstructor
public class AccessTokenProviderImpl implements AccessTokenProvider {

    private final CommonProperties commonProperties;

    @Override
    public String generateAccessToken(UUID userId, String role) {
        try {
            String secret = System.getenv().get("APP_JWT_SECRET");
            if (secret == null || secret.isEmpty())
                throw new IllegalStateException("APP_JWT_SECRET is not set");

            LocalDateTime now = LocalDateTime.now();
            long iat = now.toEpochSecond(ZoneOffset.UTC);
            long exp = now.plusMinutes(commonProperties.getAppAccessTokenTtlMinutes())
                    .toEpochSecond(ZoneOffset.UTC);

            String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String payloadJson = "{\"sub\":\"" + userId + "\",\"role\":\"" + role + "\",\"iat\":" + iat + ",\"exp\":" + exp + "}";

            String encodedHeader = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
            String encodedPayload = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

            String headerAndPayload = encodedHeader + "." + encodedPayload;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signature = mac.doFinal(headerAndPayload.getBytes(StandardCharsets.UTF_8));
            String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature);

            return headerAndPayload + "." + encodedSignature;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate access token", e);
        }
    }

    @Override
    public UUID extractUserId(String token) {
        try {
            String payloadJson = parseAndVerifyPayload(token);
            String sub = payloadJson.replaceAll(".*\"sub\":\"([^\"]+)\".*", "$1");
            return UUID.fromString(sub);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to extract userId from token", e);
        }
    }

    @Override
    public boolean isValid(String token) {
        try {
            String payloadJson = parseAndVerifyPayload(token);
            String expRaw = payloadJson.replaceAll(".*\"exp\":([0-9]+).*", "$1");
            long exp = Long.parseLong(expRaw);
            long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            return exp > now;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public LocalDateTime extractExpiresAt(String token) {
        try {
            String payloadJson = parseAndVerifyPayload(token);
            String expRaw = payloadJson.replaceAll(".*\"exp\":([0-9]+).*", "$1");
            long exp = Long.parseLong(expRaw);
            return LocalDateTime.ofEpochSecond(exp, 0, ZoneOffset.UTC);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to extract expiresAt from token", e);
        }
    }

    private String parseAndVerifyPayload(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String secret = System.getenv().get("APP_JWT_SECRET");
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("APP_JWT_SECRET is not set");
        }

        String headerAndPayload = parts[0] + "." + parts[1];
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        String expectedSignature = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(mac.doFinal(headerAndPayload.getBytes(StandardCharsets.UTF_8)));

        if (!expectedSignature.equals(parts[2])) {
            throw new IllegalArgumentException("Invalid JWT signature");
        }

        return new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
    }
}
