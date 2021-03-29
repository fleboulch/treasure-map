package com.fleboulch.treasuremap.ddd.exposition;

import com.fleboulch.treasuremap.ddd.app.StudentDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = StudentApi.class)
public class StudentExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentExceptionHandler.class);

    @ExceptionHandler(value = StudentDoesNotExistException.class)
    public ResponseEntity<?> handleNotFound(Exception exception) {
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
