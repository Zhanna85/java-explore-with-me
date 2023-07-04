package ru.practicum.requests.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.requests.EventRequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.PATTERN_CREATED_DATE;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ParticipationRequestDto {

    @DateTimeFormat(pattern = PATTERN_CREATED_DATE)
    private LocalDateTime created; // Дата и время создания заявки;
    private Long event; // Идентификатор события
    private Long id; // Идентификатор заявки
    private Long requester; // Идентификатор пользователя, отправившего заявку
    private EventRequestStatus status; // Статус заявки.
}