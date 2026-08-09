package ua.vg.msg.userservice.service.exception;

import java.io.Serial;

/**
 * UserAlreadyRegisteredException — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
public class UserAlreadyRegisteredException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6620098423390218072L;

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
