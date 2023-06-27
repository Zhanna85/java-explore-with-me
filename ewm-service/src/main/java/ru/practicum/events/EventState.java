package ru.practicum.events;

public enum EventState {
    // Список состояний жизненного цикла события
    PENDING, // Ожидает рассмотрения
    PUBLISHED, // Опубликовано
    CANCELED, // Отменено
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}
