package ua.vg.msg.messageservice.service.exception;

public class NotConversationMemberException extends RuntimeException {
    public NotConversationMemberException(String message) {
        super(message);
    }
}
