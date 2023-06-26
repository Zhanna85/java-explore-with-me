package ru.practicum.events.dto;

import lombok.Setter;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

@Setter
public class EventRequestStatusUpdateResult { // Результат подтверждения/отклонения заявок на участие в событии.

    private List<ParticipationRequestDto> confirmedRequests; // Подтвержденные запросы;
    private List<ParticipationRequestDto> rejectedRequests; // Отклоненные запросы.
}
