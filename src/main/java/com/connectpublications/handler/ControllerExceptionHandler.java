package com.connectpublications.handler;

import com.connectpublications.exception.ErrorMessage;
import com.connectpublications.exception.PublicationNotFoundException;
import com.connectpublications.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.warn("{}", errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(PublicationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlePublicationNotFoundException(PublicationNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.warn("{}", errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
