package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events; // Список идентификаторов событий входящих в подборку;
    private Boolean pinned; // Закреплена ли подборка на главной странице сайта;

    @NotBlank(message = "Field: title. Error: must not be blank. Value: null")
    @Size(min = 1, max = 50)
    private String title; // Заголовок подборки.
}