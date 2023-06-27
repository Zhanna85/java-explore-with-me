package ru.practicum.events;

import java.util.Optional;

public enum Sort {
    // Сортировка по дате
    EVENT_DATE,
    // Сортировка по количеству просмотров
    VIEWS;

    public static Optional<Sort> from(String stringState) {
        for (Sort state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
