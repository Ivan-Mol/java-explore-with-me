package ru.practicum.main.compilation.dto;

import lombok.Data;
import ru.practicum.main.event.dto.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
