package ru.practicum.requests.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.requests.EventRequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.PATTERN_CREATED_DATE;

@Setter
@ToString
@AllArgsConstructor
public class ParticipationRequestDto {

    @DateTimeFormat(pattern = PATTERN_CREATED_DATE)
    private LocalDateTime created; // Дата и время создания заявки;
    private long event; // Идентификатор события
    private long id; // Идентификатор заявки
    private long requester; // Идентификатор пользователя, отправившего заявку
    private EventRequestStatus status; // Статус заявки.
}