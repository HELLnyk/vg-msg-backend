package ua.vg.msg.userservice.dto.user;

import lombok.Builder;
import lombok.Value;
import ua.vg.msg.shared.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserDTO — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Value
@Builder
public class UserResponse {

    UUID id;
    String name;
    String email;
    UserType userType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
