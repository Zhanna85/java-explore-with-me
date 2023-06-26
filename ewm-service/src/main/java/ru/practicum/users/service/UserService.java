package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserDto;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(NewUserDto userDto);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUserById(Long userId);
}