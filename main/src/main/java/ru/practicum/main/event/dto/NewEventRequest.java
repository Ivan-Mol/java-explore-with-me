package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewEventRequest {
    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;
    @NotBlank
    @Length(min = 3, max = 120)
    String title;
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    @NotNull
    Long category;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
}