package ua.vg.msg.userservice.service;

import ua.vg.msg.userservice.dto.user.AddressRequest;
import ua.vg.msg.userservice.dto.user.AddressResponse;
import ua.vg.msg.userservice.dto.user.UserDetailResponse;
import ua.vg.msg.userservice.dto.user.UserRequest;
import ua.vg.msg.userservice.dto.user.UserResponse;
import ua.vg.msg.userservice.repository.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * UserService — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    AddressResponse addAddressToUser(UUID id, AddressRequest address);

    UserDetailResponse getUserDetails(UUID id);

    Optional<UserEntity> getUserByEmail(String email);

    Optional<UserEntity> getUserById(UUID id);

}
