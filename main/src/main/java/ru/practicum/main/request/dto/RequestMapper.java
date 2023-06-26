package ru.practicum.main.request.dto;

import ru.practicum.main.request.model.Request;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setStatus(request.getStatus());
        requestDto.setEvent(request.getEvent().getId());
        return requestDto;
    }
}