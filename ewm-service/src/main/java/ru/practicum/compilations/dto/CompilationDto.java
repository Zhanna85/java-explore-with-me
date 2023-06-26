package ru.practicum.compilations.dto;

import ru.practicum.events.dto.EventShortDto;

import java.util.HashSet;
import java.util.Set;

public class CompilationDto {

    private Set<EventShortDto> events = new HashSet<>(); // Список событий входящих в подборку;
    private long id; // Идентификатор;
    private boolean pinned; // Закреплена ли подборка на главной странице сайта;
    private String title; // Заголовок подборки.
}