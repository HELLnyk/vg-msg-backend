package ua.vg.msg.userservice.service.exception;

/**
 * UserAlreadyRegisteredException — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
public class UserAlreadyRegisteredException extends RuntimeException {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
