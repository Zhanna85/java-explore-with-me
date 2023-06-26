package ru.practicum.compilations.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class NewCompilationDto {

    private Set<Long> events; // Список идентификаторов событий входящих в подборку;
    private boolean pinned = false; // Закреплена ли подборка на главной странице сайта;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title; // Заголовок подборки.
}