package com.example.ledgerlift.exception;

import com.example.ledgerlift.base.BasedError;
import com.example.ledgerlift.base.BasedErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> handleServiceError(ResponseStatusException e) {

        BasedError<String> basedError = new BasedError<>();
        basedError.setCode(e.getStatusCode().toString());
        basedError.setDescription(e.getMessage());

        BasedErrorResponse basedErrorResponse = new BasedErrorResponse();
        basedErrorResponse.setError(basedError);

        return ResponseEntity.status(e.getStatusCode()).body(basedErrorResponse);

    }

}
