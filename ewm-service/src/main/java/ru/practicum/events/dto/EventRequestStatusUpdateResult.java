package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventRequestStatusUpdateResult { // Результат подтверждения/отклонения заявок на участие в событии.

    private List<ParticipationRequestDto> confirmedRequests; // Подтвержденные запросы;
    private List<ParticipationRequestDto> rejectedRequests; // Отклоненные запросы.
}