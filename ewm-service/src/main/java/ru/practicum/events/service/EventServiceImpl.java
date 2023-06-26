package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PaginationSetup;
import ru.practicum.events.dto.*;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.handler.NotFoundException;
import ru.practicum.handler.ViolationDateException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.events.dto.EventMapper.mapToEventFullDto;
import static ru.practicum.events.dto.EventMapper.mapToNewEvent;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private void validDate(LocalDateTime eventDate) {
        //дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ViolationDateException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDate);
        }
    }

    @Override
    public List<EventShortDto> getAllEventsByUserId(Long userId, int from, int size) {
        return eventRepository.findAllWithInitiatorByInitiatorId(userId, new PaginationSetup(from, size,
                Sort.unsorted())).stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto saveEventByIdUser(Long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        LocalDateTime eventDate = eventDto.getEventDate();

        validDate(eventDate);
        return mapToEventFullDto(eventRepository.save(mapToNewEvent(eventDto, user)), user);
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventById(Long userId, Long eventId, UpdateEventDto eventDto) {
        return null;
    }

    @Override
    public List<EventFullDto> getAllEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto updateEventByIdAdmin(Long eventId, UpdateEventDto eventDto) {
        return null;
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id) {
        return null;
    }
}
