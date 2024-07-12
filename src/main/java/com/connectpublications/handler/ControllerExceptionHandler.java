package com.connectpublications.handler;

import com.connectpublications.exception.ErrorConvertJsonStringException;
import com.connectpublications.exception.ErrorMessage;
import com.connectpublications.exception.FollowerExistException;
import com.connectpublications.exception.LoadingDataException;
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
        log.warn("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(PublicationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlePublicationNotFoundException(PublicationNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.warn("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(FollowerExistException.class)
    public ResponseEntity<ErrorMessage> handleFollowerExistException(FollowerExistException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.warn("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(ErrorConvertJsonStringException.class)
    public ResponseEntity<ErrorMessage> handleErrorConvertJsonStringException(ErrorConvertJsonStringException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.error("Error convert message to string: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(LoadingDataException.class)
    public ResponseEntity<ErrorMessage> handleLoadingDataException(LoadingDataException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        log.error("There was an error loading data: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
