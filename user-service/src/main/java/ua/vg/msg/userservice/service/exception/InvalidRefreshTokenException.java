package ua.vg.msg.userservice.service.exception;

import java.io.Serial;

public class InvalidRefreshTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -973319682088023992L;

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
