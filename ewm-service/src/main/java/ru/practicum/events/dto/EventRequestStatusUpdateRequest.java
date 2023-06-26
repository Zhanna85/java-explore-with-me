package ru.practicum.events.dto;

import ru.practicum.requests.EventRequestStatus;

import java.util.Set;

public class EventRequestStatusUpdateRequest {

    private Set<Integer> requestIds; // Идентификаторы запросов на участие в событии текущего пользователя;
    private EventRequestStatus status; // Статус заявки.
}