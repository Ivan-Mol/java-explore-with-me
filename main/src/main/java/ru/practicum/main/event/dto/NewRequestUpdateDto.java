package ru.practicum.main.event.dto;

import lombok.Data;
import ru.practicum.main.request.model.RequestStatus;

import java.util.List;

@Data
public class NewRequestUpdateDto {

    private List<Long> requestIds;

    private RequestStatus status;
}