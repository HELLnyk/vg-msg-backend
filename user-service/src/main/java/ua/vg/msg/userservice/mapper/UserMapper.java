package ua.vg.msg.userservice.mapper;

import org.mapstruct.Mapper;
import ua.vg.msg.userservice.dto.UserRequest;
import ua.vg.msg.userservice.dto.UserResponse;
import ua.vg.msg.userservice.repository.entity.UserEntity;

/**
 * UserMapper — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserRequest userRequest);

    UserResponse toResponse(UserEntity userEntity);
}
