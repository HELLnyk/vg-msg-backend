package ua.vg.msg.messageservice.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.vg.msg.messageservice.service.exception.ConversationNotFoundException;
import ua.vg.msg.messageservice.service.exception.NotConversationMemberException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * CommonExcepionHandler — TODO.
 *
 * @author ykalapusha
 * @since 10.08.2026
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConversationNotFoundException.class)
    ProblemDetail handleUserNotFoundException(ConversationNotFoundException ex) {
        log.info("Conversation Not Found exception raised");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(java.net.URI.create("conversation-not-found"));
        problemDetail.setTitle("Conversation Not Found");
        return problemDetail;
    }

    @ExceptionHandler(NotConversationMemberException.class)
    ProblemDetail handleNotConversationMemberException(NotConversationMemberException ex) {
        log.info("Not Conversation Member exception raised");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(FORBIDDEN, ex.getMessage());
        problemDetail.setType(java.net.URI.create("not-conversation-member"));
        problemDetail.setTitle("Not Conversation Member");
        return problemDetail;
    }
}
