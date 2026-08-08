package ua.vg.msg.userservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.vg.msg.userservice.dto.AddressRequest;
import ua.vg.msg.userservice.dto.AddressResponse;
import ua.vg.msg.userservice.dto.UserDetailResponse;
import ua.vg.msg.userservice.dto.UserRequest;
import ua.vg.msg.userservice.dto.UserResponse;
import ua.vg.msg.userservice.mapper.AddressMapper;
import ua.vg.msg.userservice.mapper.UserMapper;
import ua.vg.msg.userservice.repository.AddressRepository;
import ua.vg.msg.userservice.repository.UserRepository;
import ua.vg.msg.userservice.repository.entity.UserEntity;
import ua.vg.msg.userservice.service.exception.UserAlreadyRegisteredException;
import ua.vg.msg.userservice.service.exception.UserNotFoundException;

import java.util.Optional;
import java.util.UUID;

/**
 * UserServiceImpl — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@Service
@RequiredArgsConstructor
@Slf4j

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final UserMapper userMapper;

    private final AddressMapper addressMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating user: {}", userRequest);

        Optional<UserEntity> userEntityExist = userRepository.findByEmail(userRequest.getEmail());
        if (userEntityExist.isPresent())
            throw new UserAlreadyRegisteredException("User with email " + userRequest.getEmail() + " already exists");

        var userEntity = userMapper.toEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        var savedUser = userRepository.save(userEntity);
        log.info("User created: {}", savedUser);
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    @Override
    public AddressResponse addAddressToUser(UUID id, AddressRequest address) {
        log.info("Adding address to user {}: {}", id, address);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        var addressEntity = addressMapper.toEntity(address);
        addressEntity.setUser(user);
        var savedAddress = addressRepository.save(addressEntity);
        log.info("Address added to user {}: {}", id, savedAddress);
        return addressMapper.toResponse(savedAddress);
    }


    @Transactional
    @Override
    public UserDetailResponse getUserDetails(UUID id) {
        log.info("Getting user details for user {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return UserDetailResponse.builder()
                .userResponse(userMapper.toResponse(user))
                .addressResponses(user.getAddresses().stream()
                    .map(addressMapper::toResponse)
                    .toList())
                .build();
    }
}
