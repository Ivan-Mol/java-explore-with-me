package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.event.dto.NewRequestUpdateDto;
import ru.practicum.main.event.dto.RequestUpdateResponseDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.exception.exceptions.ConflictException;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestStatus;
import ru.practicum.main.request.storage.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserRepository;
import ru.practicum.main.utils.ValidationHelper;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ValidationHelper validationHelper;


    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isRequestAlreadyCreated(userId, eventId);
        validationHelper.isEventAvailable(event);
        validationHelper.isRequestAuthorInitiator(event, requester);
        validationHelper.isParticipantLimitFull(event);

        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        if (event.getRequestModeration() && event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        eventRepository.save(event);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }


    @Override
    @Transactional
    public List<RequestDto> getRequestsByUser(Long userId) {
        userRepository.getByIdAndCheck(userId);
        return requestRepository.getAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestUpdateResponseDto updateRequestStatus(Long userId, Long eventId, NewRequestUpdateDto newRequestUpdateDto) {
        User user = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isUserNotOwnerOfEvent(user, event);
        RequestUpdateResponseDto responseDto = new RequestUpdateResponseDto();
        List<Request> requestsFromStorage = requestRepository.getAllByIdIn(newRequestUpdateDto.getRequestIds());

        for (Request request : requestsFromStorage) {
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                break;
            }
            validationHelper.isRequestAlreadyConfirmed(newRequestUpdateDto, request);

            switch (newRequestUpdateDto.getStatus()) {
                case REJECTED:
                    request.setStatus(RequestStatus.REJECTED);
                    responseDto.getRejectedRequests().add((RequestMapper.toRequestDto(request)));
                    break;
                case CONFIRMED:
                    if (event.getParticipantLimit() == 0 || event.getConfirmedRequests() < event.getParticipantLimit()) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        responseDto.getConfirmedRequests().add(RequestMapper.toRequestDto(request));
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        responseDto.getRejectedRequests().add(RequestMapper.toRequestDto(request));
                        requestRepository.save(request);
                        throw new ConflictException("Participant limit is full UPDATE REQUEST");
                    }
            }
        }
        eventRepository.save(event);
        requestRepository.saveAll(requestsFromStorage);
        return responseDto;
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.getByIdAndCheck(requestId);
        User user = userRepository.getByIdAndCheck(userId);
        validationHelper.isUserRequestOwner(user, request);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}