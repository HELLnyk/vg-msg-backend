package ua.vg.msg.shared.contract.messaging.v1.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.util.List;
import java.util.UUID;

/**
 * PostMessageRequest — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@Value
public class PostMessageRequest {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int version = 1;
    @NotNull
    UUID conversationId;
    @NotNull
    UUID clientMessageId;
    @NotBlank
    @Size(min = 1, max = 4000)
    String text;
    @Valid
    List<AttachmentDto> attachments;
}
