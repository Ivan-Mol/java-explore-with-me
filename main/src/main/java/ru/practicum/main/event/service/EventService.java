package ru.practicum.main.event.service;

import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEventDto, Long userId);

    EventFullDto getEvent(Long id, Long userId);

    List<EventFullDto> getAllEvents(Long userId, Integer from, Integer size);

    EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest eventUpdateDto);
}
