package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static ru.practicum.Constants.PATTERN_DATE;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException notFoundException) {
        log.error("Код ошибки: {}, {}", HttpStatus.NOT_FOUND, notFoundException.getMessage());
        return Map.of(
                "status", "NOT_FOUND",
                "reason", "The required object was not found.",
                "message", notFoundException.getMessage(),
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_DATE))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidException(final MethodArgumentNotValidException exception) {
        log.error("Код ошибки: {}, {}", HttpStatus.BAD_REQUEST, exception.getMessage());
        return Map.of(
                "status", "BAD_REQUEST",
                "reason", "Incorrectly made request.",
                "message", exception.getMessage(),
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_DATE))
        );
    }

    @ExceptionHandler({ConstraintViolationException.class, NotEmptyException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConstraintViolationException(final RuntimeException exception) {
        log.error("Код ошибки: {}, {}", HttpStatus.CONFLICT, exception.getMessage());
        return Map.of(
                "status", "CONFLICT",
                "reason", "Integrity constraint has been violated.",
                "message", exception.getMessage(),
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_DATE))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleViolationDateException(final ViolationDateException exception) {
        log.error("Код ошибки: {}, {}", HttpStatus.BAD_REQUEST, exception.getMessage());
        return Map.of(
                "status", "BAD_REQUEST",
                "reason", "For the requested operation the conditions are not met.",
                "message", exception.getMessage(),
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_DATE))
        );
    }
}