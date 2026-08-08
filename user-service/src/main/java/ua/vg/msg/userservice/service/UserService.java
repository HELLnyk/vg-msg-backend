package ua.vg.msg.userservice.service;

import ua.vg.msg.userservice.dto.AddressRequest;
import ua.vg.msg.userservice.dto.AddressResponse;
import ua.vg.msg.userservice.dto.UserDetailResponse;
import ua.vg.msg.userservice.dto.UserRequest;
import ua.vg.msg.userservice.dto.UserResponse;

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

}
