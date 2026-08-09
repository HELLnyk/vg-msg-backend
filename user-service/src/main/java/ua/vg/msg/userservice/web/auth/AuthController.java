package ua.vg.msg.userservice.web.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vg.msg.userservice.dto.auth.LoginRequest;
import ua.vg.msg.userservice.dto.auth.LoginResponse;
import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;
import ua.vg.msg.userservice.dto.auth.RefreshTokenResponse;
import ua.vg.msg.userservice.service.AuthService;

/**
 * AuthController — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
