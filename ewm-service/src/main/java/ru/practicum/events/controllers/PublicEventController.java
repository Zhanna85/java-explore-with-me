package ru.practicum.events.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.Constants.PATTERN_DATE;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<EventShortDto> getEventsPublic(
            // текст для поиска в содержимом аннотации и подробном описании события
            @RequestParam String text,

            // список идентификаторов категорий в которых будет вестись поиск
            @RequestParam(defaultValue = "") List<Long> categories,

            // поиск только платных/бесплатных событий
            @RequestParam(defaultValue = "--") Boolean paid,

            // дата и время не раньше которых должно произойти событие
            @RequestParam(value = "rangeStart") @FutureOrPresent
            @DateTimeFormat(pattern = PATTERN_DATE) LocalDateTime rangeStart,

            // дата и время не позже которых должно произойти событие
            @RequestParam(value = "rangeEnd") @Future @DateTimeFormat(pattern = PATTERN_DATE) LocalDateTime rangeEnd,

            // только события у которых не исчерпан лимит запросов на участие
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,

            // Вариант сортировки: по дате события или по количеству просмотров
            @RequestParam(defaultValue = "--") String sort,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Get events with parameters: text {}, categories {}, paid {}, rangeStart {}, rangeEnd {}, " +
                "onlyAvailable {}, sort {}, from {}, size {}", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto getEventByIdPublic(@PathVariable(value = "id") Long id,
                                           HttpServletRequest request) {
        log.info("Get event by Id {}", id);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getEventByIdPublic(id);
    }
}
