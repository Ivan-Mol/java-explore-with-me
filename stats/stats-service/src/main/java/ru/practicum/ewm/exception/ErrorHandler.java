package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable e) {
        return new ErrorResponse("An unexpected error");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse emailIsInvalidException(final MethodArgumentNotValidException e) {
        FieldError error = e.getFieldError();
        if (error == null) {
            return new ErrorResponse(e.getMessage());
        } else {
            return new ErrorResponse(error.getDefaultMessage());
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse noArgsHandler(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
