package ru.practicum.main.event.dto;

import lombok.Data;
import ru.practicum.main.request.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestUpdateResponseDto {

    private List<RequestDto> confirmedRequests = new ArrayList<>();

    private List<RequestDto> rejectedRequests = new ArrayList<>();

}