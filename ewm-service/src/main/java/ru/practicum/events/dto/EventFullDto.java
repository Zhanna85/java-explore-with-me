package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.EventState;
import ru.practicum.events.model.Location;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.PATTERN_DATE;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private String annotation; // Краткое описание;
    private CategoryDto category; // Категория;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_DATE)
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String description; // Полное описание события;
    private int confirmedRequests; // Количество одобренных заявок на участие в данном событии;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_DATE)
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss");
    private Long id; // Идентификатор;
    private UserShortDto initiator; // Инициатор;
    private Location location; // Широта и долгота места проведения события;
    private boolean paid; // Нужно ли оплачивать участие;
    private int participantLimit; // Ограничение на количество участников;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_DATE)
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss");
    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие, default: true;
    private EventState state; // Список состояний жизненного цикла события;
    private String title; // Заголовок;
    private int views = 0; // Количество просмотров события.
}
