package ru.practicum.handler;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException(String message) {
        super(message);
    }
}