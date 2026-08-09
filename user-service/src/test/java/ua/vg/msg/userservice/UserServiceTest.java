package ua.vg.msg.userservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vg.msg.userservice.dto.user.AddressRequest;
import ua.vg.msg.userservice.dto.user.UserRequest;
import ua.vg.msg.userservice.dto.user.UserResponse;
import ua.vg.msg.userservice.dto.user.UserTypeRequest;
import ua.vg.msg.userservice.mapper.AddressMapper;
import ua.vg.msg.userservice.mapper.UserMapper;
import ua.vg.msg.userservice.repository.AddressRepository;
import ua.vg.msg.userservice.repository.UserRepository;
import ua.vg.msg.userservice.repository.entity.UserEntity;
import ua.vg.msg.userservice.service.UserServiceImpl;
import ua.vg.msg.userservice.service.exception.UserAlreadyRegisteredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.vg.msg.shared.UserType;
import ua.vg.msg.userservice.service.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UserServiceTest — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateUserAlreadyExists() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");

        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        Assertions.assertThrows(
                UserAlreadyRegisteredException.class,
                () -> userService.createUser(userRequest)
        );
    }

    @Test
    public void testCreateUserSuccessfully() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("plain-password");
        userRequest.setUserType(UserType.USER);

        UserEntity mappedEntity = new UserEntity();
        mappedEntity.setName(userRequest.getName());
        mappedEntity.setEmail(userRequest.getEmail());
        mappedEntity.setPassword(userRequest.getPassword());
        mappedEntity.setUserType(userRequest.getUserType());

        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(userId);
        savedEntity.setName(userRequest.getName());
        savedEntity.setEmail(userRequest.getEmail());
        savedEntity.setPassword("encoded-password");
        savedEntity.setUserType(userRequest.getUserType());
        savedEntity.setCreatedAt(now);
        savedEntity.setUpdatedAt(now);

        UserResponse expectedResponse = UserResponse.builder()
                .id(userId)
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .userType(userRequest.getUserType())
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest)).thenReturn(mappedEntity);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(userMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.createUser(userRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals("encoded-password", mappedEntity.getPassword());
        verify(userRepository).save(mappedEntity);
    }

    @Test
    public void testNotFoundUserByAddingAddress() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenThrow(new UserNotFoundException("foo"));
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.addAddressToUser(userId, new AddressRequest()));

    }

    @Test
    public void testNotFoundUserByGettingDetails() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenThrow(new UserNotFoundException("foo"));
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserDetails(userId));

    }

    @Test
    void testUpdateStatusTypeError() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenThrow(new UserNotFoundException("foo"));
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUserType(userId, new UserTypeRequest(UserType.USER)));
    }

    @Test
    void testUpdateStatusTypeSuccessfully() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUserType(UserType.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(
                UserResponse.builder()
                        .id(userId)
                        .userType(UserType.ADMIN)
                        .build()
        );

        UserResponse response = userService.updateUserType(userId, new UserTypeRequest(UserType.ADMIN));

        Assertions.assertEquals(UserType.ADMIN, response.getUserType());
    }

    @Test
    void testDeleteUser(){
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
