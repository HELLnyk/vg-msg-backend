package ua.vg.msg.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * AddressRequest — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Data
public class AddressRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    private String street;
    @NotBlank
    @Size(min = 1, max = 100)
    private String city;
    @NotBlank
    @Size(min = 1, max = 5)
    private String number;
}
