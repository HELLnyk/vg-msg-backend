package ua.vg.msg.userservice.web.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vg.msg.userservice.dto.user.AddressRequest;
import ua.vg.msg.userservice.dto.user.AddressResponse;
import ua.vg.msg.userservice.dto.user.UserDetailResponse;
import ua.vg.msg.userservice.dto.user.UserRequest;
import ua.vg.msg.userservice.dto.user.UserResponse;
import ua.vg.msg.userservice.service.UserService;

import java.util.UUID;

/**
 * UserController — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST /api/users - Creating user: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(userService.createUser(userRequest));
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<AddressResponse> addAddressToUser(@PathVariable UUID id, @Valid @RequestBody AddressRequest address) {
        log.info("POST /api/users/{}/address - Adding address: {}", id, address);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(userService.addAddressToUser(id, address));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUserDetails(@PathVariable UUID id) {
        log.info("GET /api/users/{}", id);
        return ResponseEntity.ok()
                .body(userService.getUserDetails(id));
    }

}
