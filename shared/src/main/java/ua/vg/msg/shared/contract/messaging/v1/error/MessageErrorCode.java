package ua.vg.msg.shared.contract.messaging.v1.error;

public enum MessageErrorCode {
    UNAUTHORIZED,
    NOT_CONVERSATION_MEMBER,
    CONVERSATION_NOT_FOUND,
    DUPLICATE_CLIENT_MESSAGE_ID,
    VALIDATION_ERROR,
    RATE_LIMITED;
}
