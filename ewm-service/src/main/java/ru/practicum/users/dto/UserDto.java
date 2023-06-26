package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private Long id;
    private String name;
}