package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ValidationHelper validationHelper;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isEventAvailableForRequest(event);
        validationHelper.isUserOwnerOfEvent(requester, event);
        validationHelper.isParticipantLimitFull(event);
        validationHelper.isRequestAlreadyCreated(userId, eventId);

        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(event.getParticipantLimit() == 0 ? RequestStatus.CONFIRMED : RequestStatus.PENDING);
        try {
            request = requestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Request is duplicate");
        }
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getRequestsByUser(Long userId) {
        userRepository.getByIdAndCheck(userId);
        return requestRepository.getAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestUpdateResponseDto updateRequestStatus(Long userId, Long eventId, NewRequestUpdateDto newRequestUpdateDto) {
        User user = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isParticipantLimitFull(event);
        validationHelper.isUserNotOwnerOfEvent(user, event);

        RequestUpdateResponseDto responseDto = new RequestUpdateResponseDto();
        List<Request> requestsFromStorage = requestRepository.getAllByIdIn(newRequestUpdateDto.getRequestIds());
        for (Request request : requestsFromStorage) {
            if (event.getParticipantLimit().equals(0) || !event.getRequestModeration()) {
                break;
            }
            validationHelper.isRequestAlreadyConfirmed(newRequestUpdateDto, request);

            switch (newRequestUpdateDto.getStatus()) {
                case REJECTED:
                    request.setStatus(RequestStatus.REJECTED);
                    responseDto.getRejectedRequests().add((RequestMapper.toRequestDto(request)));
                    break;
                case CONFIRMED:
                    if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests()) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        responseDto.getConfirmedRequests().add(RequestMapper.toRequestDto(request));
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        responseDto.getRejectedRequests().add(RequestMapper.toRequestDto(request));
                    }
            }
        }
        requestRepository.saveAll(requestsFromStorage);
        return responseDto;
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.getByIdAndCheck(requestId);
        User user = userRepository.getByIdAndCheck(userId);
        validationHelper.isUserRequestOwner(user, request);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}