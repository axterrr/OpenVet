package ua.edu.ukma.objectanalysis.openvet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ua.edu.ukma.objectanalysis.openvet.dto.ErrorResponse;
import ua.edu.ukma.objectanalysis.openvet.exception.BaseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getStatus(), ex.getName(), ex.getMessages()), HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(404, "Resource not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(500, "Unexpected exception", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
