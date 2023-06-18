package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {

    @NotBlank
    private String app; // Идентификатор сервиса для которого записывается информация, example: ewm-main-service;
    @NotBlank
    private String uri; // URI для которого был осуществлен запрос, example: /events/1;
    private String ip; // IP-адрес пользователя, осуществившего запрос, example: 192.163.0.1;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к эндпоинту
    // (в формате "yyyy-MM-dd HH:mm:ss"), example: 2022-09-06 11:00:23.
}