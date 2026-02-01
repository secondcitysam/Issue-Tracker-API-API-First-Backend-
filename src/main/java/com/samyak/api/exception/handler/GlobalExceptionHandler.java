package com.samyak.api.exception.handler;

import com.samyak.api.exception.AccessDeniedException;
import com.samyak.api.exception.InvalidStateException;
import com.samyak.api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // -----------------------------
    // 404
    // -----------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex
    ) {
        return new ResponseEntity<>(
                new ErrorResponse("NOT_FOUND", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    // -----------------------------
    // 403
    // -----------------------------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return new ResponseEntity<>(
                new ErrorResponse("ACCESS_DENIED", ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    // -----------------------------
    // 400 (INVALID STATE)
    // -----------------------------
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(
            InvalidStateException ex
    ) {
        return new ResponseEntity<>(
                new ErrorResponse("INVALID_STATE", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // -----------------------------
    // 400 (VALIDATION)
    // -----------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Validation error");

        return new ResponseEntity<>(
                new ErrorResponse("VALIDATION_ERROR", message),
                HttpStatus.BAD_REQUEST
        );
    }

    // -----------------------------
    // 500 (SAFETY NET)
    // -----------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex
    ) {
        return new ResponseEntity<>(
                new ErrorResponse("INTERNAL_ERROR", "Unexpected server error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleEnumMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        String message = "Invalid value for parameter '" + ex.getName() + "'";

        return new ResponseEntity<>(
                new ErrorResponse("INVALID_REQUEST", message),
                HttpStatus.BAD_REQUEST
        );
    }

}
