package ua.vg.msg.userservice.service.exception;

/**
 * UserNotFoundException — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
