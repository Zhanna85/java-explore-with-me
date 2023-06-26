package ru.practicum.users.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody NewUserDto userDto) {
        log.info("Creating user {}", userDto);
        return userService.saveUser(userDto);
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<UserDto> getUsers(@RequestParam(defaultValue = " ") List<Long> ids,
                                        @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Get users by ids {}. Parameters: from {}, size {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        log.info("Deleting user by Id {}", userId);
        userService.deleteUserById(userId);
    }
}