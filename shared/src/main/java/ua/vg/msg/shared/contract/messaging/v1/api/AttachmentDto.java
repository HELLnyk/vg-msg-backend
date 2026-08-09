package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

/**
 * AttachmentDto — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Value
public class AttachmentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version = 1;
    @NotNull
    UUID id;
    @NotBlank
    String type;
}
