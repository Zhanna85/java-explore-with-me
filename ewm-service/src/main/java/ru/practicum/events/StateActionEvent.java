package ru.practicum.events;

public enum StateActionEvent {
    // Новое состояние события (указывается в запросе на редактирование от администратора)
    PUBLISH_EVENT, // Статус состояния события опубликовано
    REJECT_EVENT // Статус состояния события отклонено
}
