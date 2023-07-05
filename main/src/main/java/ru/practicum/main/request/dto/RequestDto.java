package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    Long id;

    Long event;

    Long requester;

    RequestStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
}
