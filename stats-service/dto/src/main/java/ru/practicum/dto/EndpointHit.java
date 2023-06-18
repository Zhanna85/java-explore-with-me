package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private String timestamp; // Дата и время, когда был совершен запрос к эндпоинту
}