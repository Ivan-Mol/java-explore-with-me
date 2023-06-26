package ru.practicum.main.event.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class LocationDto {

    @NotNull
    Double lat;

    @NotNull
    Double lon;
}