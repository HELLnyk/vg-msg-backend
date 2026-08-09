package ua.vg.msg.userservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import ua.vg.msg.shared.UserType;

/**
 * UserStatusRequest — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Value
public class UserTypeRequest {
    @NotBlank
    UserType userType;
}
