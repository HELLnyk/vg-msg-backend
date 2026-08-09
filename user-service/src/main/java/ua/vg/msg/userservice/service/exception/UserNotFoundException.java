package ua.vg.msg.userservice.service.exception;

import java.io.Serial;

/**
 * UserNotFoundException — TODO.
 *
 * @author ykalapusha
 * @since 08.08.2026
 */
public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8584825080264430640L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
