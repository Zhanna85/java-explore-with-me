package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.util.PaginationSetup;
import ru.practicum.StatsClient;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.StateActionEvent;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.handler.NotFoundException;
import ru.practicum.handler.ValidateException;
import ru.practicum.handler.ValidateDateException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.events.Sort.EVENT_DATE;
import static ru.practicum.util.Constants.APP;
import static ru.practicum.util.Messages.*;
import static ru.practicum.events.EventState.*;
import static ru.practicum.events.dto.EventMapper.mapToEventFullDto;
import static ru.practicum.events.dto.EventMapper.mapToNewEvent;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    private void validDate(LocalDateTime eventDate) {
        //дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidateDateException("Field: eventDate. Error: должно содержать дату, которая еще не наступила." +
                    " Value: " + eventDate);
        }
    }

    private Category getCategoryForEvent(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found"));
    }

    private Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));
    }

    private Event getEventByIdAndInitiatorId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private void updateEvent(Event event, UpdateEventDto eventDto) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = getCategoryForEvent(eventDto.getCategory());
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            validDate(eventDto.getEventDate());
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(StateActionEvent.CANCEL_REVIEW)) {
                event.setState(CANCELED);
            }
            if (eventDto.getStateAction().equals(StateActionEvent.SEND_TO_REVIEW)) {
                event.setState(PENDING);
            }
        }
        if (eventDto.getLocation() != null) {
            event.setLat(eventDto.getLocation().getLat());
            event.setLon(eventDto.getLocation().getLon());
        }
    }

    private void updateEventAdmin(Event event, UpdateEventDto eventDto) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            Category category = getCategoryForEvent(eventDto.getCategory());
            event.setCategory(category);
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getStateAction() != null) {
           /* событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
            событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)*/
            if (event.getState().equals(PENDING)) {
                if (eventDto.getStateAction().equals(StateActionEvent.REJECT_EVENT)) {
                    event.setState(CANCELED);
                }
                if (eventDto.getStateAction().equals(StateActionEvent.PUBLISH_EVENT)) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            } else {
                throw new ValidateException("Cannot publish or cancel the event because it's not in the right state: " +
                        event.getState());
            }
        }
        if (eventDto.getLocation() != null) {
            event.setLat(eventDto.getLocation().getLat());
            event.setLon(eventDto.getLocation().getLon());
        }
        // Дата начала изменяемого события должна быть не ранее чем за час от даты публикации.
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isAfter(event.getPublishedOn().plusHours(1))) {
            event.setEventDate(eventDto.getEventDate());
        } else {
            throw new ValidateDateException("The start date of the event being modified must be no earlier than an hour " +
                    "from the date of publication.");
        }
    }

    private void validDateParam(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateDateException("The range start date cannot be is after range end date");
            }
        }
    }

    @Override
    public List<EventShortDto> getAllEventsByUserId(Long userId, int from, int size) {
        log.info(GET_MODELS.getMessage());
        return eventRepository.findAllWithInitiatorByInitiatorId(userId, new PaginationSetup(from, size,
                Sort.unsorted())).stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto saveEventByIdUser(Long userId, NewEventDto eventDto) {
        validDate(eventDto.getEventDate());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Category category = getCategoryForEvent(eventDto.getCategory());
        Event event = eventRepository.save(mapToNewEvent(eventDto, user, category));
        log.info(SAVE_MODEL.getMessage(), event);
        return mapToEventFullDto(event);
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = getEventByIdAndInitiatorId(eventId, userId);
        return mapToEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventById(Long userId, Long eventId, UpdateEventDto eventDto) {
        Event event = getEventByIdAndInitiatorId(eventId, userId);

        /*изменить можно только отмененные события или события в состоянии ожидания модерации, дата и время на которые
        намечено событие не может быть раньше, чем через два часа от текущего момента*/
        if (event.getState() == PUBLISHED || event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidateException("You can only change events in the state of CANCELED or events in " +
                    "the state of PENDING. And the date and time for which the event is scheduled cannot be earlier than" +
                    " two hours from the current moment");
        }
        updateEvent(event, eventDto);
        log.info(UPDATE_MODEL.getMessage(), event);
        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users,
                                                List<String> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Integer from,
                                                Integer size) {
        log.info(GET_MODELS.getMessage());
        return null;
    }

    @Override
    public EventFullDto updateEventByIdAdmin(Long eventId, UpdateEventDto eventDto) {
        Event event = getEventById(eventId);
        updateEventAdmin(event, eventDto);
        log.info(UPDATE_MODEL.getMessage(), event);
        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               Sort sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        log.info(GET_MODELS.getMessage());
        validDateParam(rangeStart, rangeEnd);
        PaginationSetup pageable;

        if (sort.equals(EVENT_DATE)) {
            pageable = new PaginationSetup(from, size, Sort.by("eventDate"));
        } else {
            pageable = new PaginationSetup(from, size, Sort.unsorted());
        }

        LocalDateTime currentDate = LocalDateTime.now();
        List<Event> events = eventRepository.findAllPublish(text, categories, paid, rangeStart, rangeEnd, currentDate,
                onlyAvailable, pageable);
        // это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
        // текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв

        // информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
        // информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        statsClient.saveStats(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return null;
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id,
                                           HttpServletRequest request) {
        log.info(GET_MODEL_BY_ID.getMessage(), id);
        Event event = getEventById(id);
        // событие должно быть опубликовано
        if (!event.getState().equals(PUBLISHED)) {
            throw new ValidateException("Event with id=" + id + " not published");
        }
        // информация о событии должна включать в себя количество просмотров
        statsClient.getStats();
        // информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        statsClient.saveStats(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return mapToEventFullDto(eventRepository.save(event));
    }
}