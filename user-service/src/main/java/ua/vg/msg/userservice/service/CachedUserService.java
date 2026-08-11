package ua.vg.msg.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.vg.msg.userservice.dto.user.UserResponse;
import ua.vg.msg.userservice.mapper.UserMapper;
import ua.vg.msg.userservice.repository.UserRepository;
import ua.vg.msg.userservice.service.exception.UserNotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CachedUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Cacheable(value = "user", key = "#id")
    public UserResponse getUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }
}
