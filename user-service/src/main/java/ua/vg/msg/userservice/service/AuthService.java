package ua.vg.msg.userservice.service;

import ua.vg.msg.userservice.dto.auth.LoginRequest;
import ua.vg.msg.userservice.dto.auth.LoginResponse;
import ua.vg.msg.userservice.dto.auth.RefreshTokenRequest;
import ua.vg.msg.userservice.dto.auth.RefreshTokenResponse;

/**
 * AuthService — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
