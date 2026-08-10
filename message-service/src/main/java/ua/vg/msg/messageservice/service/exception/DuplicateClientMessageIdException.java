package ua.vg.msg.messageservice.service.exception;

public class DuplicateClientMessageIdException extends RuntimeException {
    public DuplicateClientMessageIdException(String message) {
        super(message);
    }
}
