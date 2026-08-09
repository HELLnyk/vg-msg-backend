package ua.vg.msg.userservice.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * LoginRequest — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Value
public class LoginRequest {
    @NotBlank
    String email;
    @NotBlank
    String password;
}
