package ru.practicum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Messages {

    NOT_FOUND_USER("User with id={} was not found");

    private final String message;
}