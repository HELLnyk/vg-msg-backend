package ua.vg.msg.userservice.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * AddressResponse — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Value
@Builder
public class AddressResponse {
    Long id;
    String street;
    String city;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
