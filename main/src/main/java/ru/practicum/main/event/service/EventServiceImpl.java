package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryMapper;
import ru.practicum.main.category.storage.CategoryRepository;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventMapper;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventUserRequest;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.model.StateAction;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.user.storage.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(userRepository.getByIdAndCheck(userId));
        event.setCategory(categoryRepository.getByIdAndCheck(newEventDto.getCategory()));
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEvent(Long id,Long userId) {
        userRepository.getByIdAndCheck(userId);
        return EventMapper.toEventFullDto(eventRepository.getByIdAndCheck(id));
    }

    @Override
    public List<EventFullDto> getAllEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository
                .getByInitiatorId(userId, pageable)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest eventUpdateDto) {
        userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Event eventForUpdate = eventRepository.findById(eventId).orElseThrow(RuntimeException::new);

        if (eventUpdateDto.getEventDate() != null
                && eventUpdateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date is incorrect");
        }
        if (eventForUpdate.getState() == EventState.PUBLISHED) {
            throw new ValidationException("Event is already published");
        }

        if (!userId.equals(eventForUpdate.getInitiator().getId())) {
            throw new NotFoundException("Event is not found");
        }
        if (eventUpdateDto.getAnnotation() != null) {
            eventForUpdate.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (eventUpdateDto.getCategoryDto() != null) {
            eventForUpdate.setCategory(eventForUpdate.getCategory());
        }
        if (eventUpdateDto.getDescription() != null) {
            eventForUpdate.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getEventDate() != null) {
            eventForUpdate.setEventDate(eventUpdateDto.getEventDate());
        }
        if (eventUpdateDto.getLocation() != null) {
            eventForUpdate.setPaid(eventUpdateDto.getPaid());
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            eventForUpdate.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (eventUpdateDto.getRequestModeration() != null) {
            eventForUpdate.setRequestModeration(eventUpdateDto.getRequestModeration());
        }
        if (eventUpdateDto.getStateAction() != null) {
            if (eventUpdateDto.getStateAction() == StateAction.SEND_TO_REVIEW) {
                eventForUpdate.setState(EventState.PENDING);
            } else if (eventUpdateDto.getStateAction() == StateAction.CANCEL_REVIEW) {
                eventForUpdate.setState(EventState.CANCELED);
            }
        }
        if (eventUpdateDto.getTitle() != null) {
            eventForUpdate.setTitle(eventUpdateDto.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(eventForUpdate));
    }


}
