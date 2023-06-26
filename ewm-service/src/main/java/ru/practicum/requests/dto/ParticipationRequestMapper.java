package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.events.model.Event;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.users.model.User;

@UtilityClass
public class ParticipationRequestMapper {

    public static ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public static ParticipationRequest mapToNewParticipationRequest(Event event, User user) {
        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);

        return request;
    }
}
