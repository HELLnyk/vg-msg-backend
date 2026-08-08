package ua.vg.msg.userservice.dto;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * UserDetailResponse — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Value
@Builder
public class UserDetailResponse {
    UserResponse userResponse;
    List<AddressResponse> addressResponses;
}
