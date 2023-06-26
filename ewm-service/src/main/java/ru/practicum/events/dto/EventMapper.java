package ru.practicum.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.users.model.User;

import static ru.practicum.categories.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.users.dto.UserMapper.toUserShortDto;

@UtilityClass
public class EventMapper {

    public static Event mapToNewEvent(NewEventDto eventDto, User user) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        Category category = new Category();
        category.setId(eventDto.getCategory());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setLat(eventDto.getLocation().getLat());
        event.setLon(eventDto.getLocation().getLon());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setInitiator(user);

        return event;
    }

    public static EventFullDto mapToEventFullDto(Event event, User user) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(toCategoryDto(event.getCategory()));
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(toUserShortDto(user));
        Location location = new Location(event.getLat(), event.getLon());
        eventFullDto.setLocation(location);
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        /*добавить
        eventFullDto.setConfirmedRequests();
        eventFullDto.setViews();*/

        return eventFullDto;
    }

    public static EventShortDto mapToEventShortDto(Event event) {
        EventShortDto shortDto = new EventShortDto();
        shortDto.setAnnotation(event.getAnnotation());
        shortDto.setCategory(toCategoryDto(event.getCategory()));
        shortDto.setEventDate(event.getEventDate());
        shortDto.setId(event.getId());
        shortDto.setInitiator(toUserShortDto(event.getInitiator()));
        shortDto.setPaid(event.getPaid());
        shortDto.setTitle(event.getTitle());
        /*добавить
        shortDto.setConfirmedRequests();
        shortDto.setViews();*/

        return shortDto;
    }
}