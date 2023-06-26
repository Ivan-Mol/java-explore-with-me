package ru.practicum.main.request.service;

import ru.practicum.main.event.dto.NewRequestUpdateDto;
import ru.practicum.main.event.dto.RequestUpdateResponseDto;
import ru.practicum.main.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByUser(Long userId);

    RequestUpdateResponseDto updateRequestStatus(Long userId, Long eventId, NewRequestUpdateDto statusUpdateRequest);

    RequestDto cancelRequest(Long userId, Long requestId);
}