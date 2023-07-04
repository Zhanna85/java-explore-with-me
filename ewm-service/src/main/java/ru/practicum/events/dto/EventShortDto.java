package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private String annotation; // Краткое описание;
    private CategoryDto category; // Категория;
    private Integer confirmedRequests; // Количество одобренных заявок на участие в данном событии;
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss");
    private Long id; // Идентификатор;
    private UserShortDto initiator; // Инициатор;
    private Boolean paid; // Нужно ли оплачивать участие;
    private String title; // Заголовок;
    private Long views; // Количество просмотров события.
}