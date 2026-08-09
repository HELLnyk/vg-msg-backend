package ua.vg.msg.userservice.service.exception;

import java.io.Serial;

/**
 * InvalidCredentialsException — TODO.
 *
 * @author ykalapusha
 * @since 09.08.2026
 */
public class InvalidCredentialsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5642567173557816521L;

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
