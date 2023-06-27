package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.handler.NotEmptyException;
import ru.practicum.handler.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.ParticipationRequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.events.EventState.PUBLISHED;
import static ru.practicum.requests.EventRequestStatus.*;
import static ru.practicum.requests.dto.ParticipationRequestMapper.mapToNewParticipationRequest;
import static ru.practicum.requests.dto.ParticipationRequestMapper.mapToParticipationRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService{

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private void validParticipantLimit(Event event) {
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests().equals(event.getParticipantLimit()) ) {
            throw new NotEmptyException("The event has reached the limit of participation requests.");
        }
    }

    private void validRequestStatus(List<ParticipationRequest> requests) {
        boolean isStatusPending = requests.stream()
                .anyMatch(request -> !request.getStatus().equals(PENDING));

        if (isStatusPending) {
            throw new NotEmptyException("The status can be changed only for requests that are in the PENDING state.");
        }
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequest(Long userId, Long eventId) {
        // Получить информацию о запросах на участие в событии текущего пользователя
        if (eventRepository.findByIdAndInitiatorId(eventId, userId).isPresent()) {
            return requestRepository.findAllByEventId(eventId).stream()
                    .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                    .collect(Collectors.toList());
        }
        // если по заданным фильтрам не найдено, возвращаем пустой список
        return Collections.emptyList();
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest dtoRequest) {
        /*

        если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить*/
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
        // если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new NotEmptyException("for the event, the application limit is 0 or pre-moderation of applications " +
                    "is disabled, confirmation of applications is not required");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndIdIn(eventId,
                dtoRequest.getRequestIds());
        // статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        validRequestStatus(requests);

        switch (dtoRequest.getStatus()) {
            case CONFIRMED:
                // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
                validParticipantLimit(event);
                int limitUpdate = event.getParticipantLimit() - event.getConfirmedRequests();
                List<ParticipationRequestDto> confirmedRequests = null;
                if (requests.size() <= limitUpdate) {
                    requests.forEach(request -> request.setStatus(CONFIRMED));
                    requestRepository.saveAll(requests);
                    confirmedRequests = requests
                            .stream()
                            .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                            .collect(Collectors.toList());
                    event.setConfirmedRequests(event.getConfirmedRequests()+requests.size());
                    eventRepository.save(event);
                } else {
                    confirmedRequests = requests
                            .stream()
                            .limit(limitUpdate)
                            .forEach(request -> request.setStatus(CONFIRMED))
                            .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                            .collect(Collectors.toList());
                }
                return new EventRequestStatusUpdateResult(confirmedRequests, List.of());
            case REJECTED:
                requests.forEach(request -> request.setStatus(REJECTED));
                requestRepository.saveAll(requests);
                List<ParticipationRequestDto> rejectedRequests = requests
                        .stream()
                        .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                        .collect(Collectors.toList());
                return new EventRequestStatusUpdateResult(List.of(), rejectedRequests);
            default:
                throw new NotEmptyException("Unknown state: " + dtoRequest.getStatus());

        }
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));

        // инициатор события не может добавить запрос на участие в своём событии
        if (userId.equals(event.getInitiator().getId())) {
            throw new NotEmptyException("The initiator of the event cannot add a request to participate " +
                    "in his event.");
        }
        // нельзя участвовать в неопубликованном событии
        if (event.getState() != PUBLISHED) {
            throw new NotEmptyException("You cannot participate in an unpublished event.");
        }
        // если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
        validParticipantLimit(event);
        // нельзя добавить повторный запрос
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new NotEmptyException("You cannot add a repeat request.");
        }
        ParticipationRequest newRequest = requestRepository.save(mapToNewParticipationRequest(event, user));

        if (newRequest.getStatus() == CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests()+1);
            eventRepository.save(event);
        }

        return mapToParticipationRequestDto(newRequest);
    }

    @Transactional
    @Override
    public ParticipationRequestDto updateStatusParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        request.setStatus(REJECTED);

        return mapToParticipationRequestDto(requestRepository.save(request));
    }
}