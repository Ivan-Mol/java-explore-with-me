package ru.practicum.main.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @NotBlank
    private Double lat;
    @NotBlank
    private Double lon;
}