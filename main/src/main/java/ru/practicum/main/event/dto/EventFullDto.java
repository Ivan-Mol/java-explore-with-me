package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventFullDto {

    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    UserShortDto initiator;
    LocationDto location;
    String title;
    EventState state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    Boolean paid;
    Boolean requestModeration;
    Integer participantLimit;
    Integer views;
}