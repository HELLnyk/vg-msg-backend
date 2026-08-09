package ua.vg.msg.userservice.mapper;

import org.mapstruct.Mapper;
import ua.vg.msg.userservice.dto.user.AddressRequest;
import ua.vg.msg.userservice.dto.user.AddressResponse;
import ua.vg.msg.userservice.repository.entity.AddressEntity;

/**
 * AddressMapper — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressEntity toEntity(AddressRequest addressRequest);
    AddressResponse toResponse(AddressEntity addressEntity);
}
