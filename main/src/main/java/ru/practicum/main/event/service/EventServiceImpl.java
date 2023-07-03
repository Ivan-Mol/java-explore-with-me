package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.StatisticClient;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.storage.CategoryRepository;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventMapper;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventRequest;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.event.dto.UpdateEventUserRequest;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.exception.exceptions.BadRequestException;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestMapper;
import ru.practicum.main.request.storage.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserRepository;
import ru.practicum.main.utils.ValidationHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final ValidationHelper validationHelper;
    private final StatisticClient statisticClient;

    private static void updateEventStatusByUser(UpdateEventUserRequest eventUserRequest, Event event) {
        if (eventUserRequest.getStateAction() != null) {
            switch (eventUserRequest.getStateAction()) {
                case "CANCEL_REVIEW":
                    event.setState(EventState.CANCELED);
                    break;
                case "SEND_TO_REVIEW":
                    event.setState(EventState.PENDING);
                    break;
                default:
                    throw new ValidationException("Incorrect Event state");
            }
        }
    }

    private static void updateEventStatusByAdmin(UpdateEventAdminRequest eventAdminRequest, Event event) {
        if (eventAdminRequest.getStateAction() != null) {
            switch (eventAdminRequest.getStateAction()) {
                case "PUBLISH_EVENT":
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case "REJECT_EVENT":
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ValidationException("Incorrect Event state");
            }
        }
    }

    @Override
    public EventFullDto createEvent(NewEventRequest newEventRequest, Long userId) {
        User user = userRepository.getByIdAndCheck(userId);
        Event event = EventMapper.toEvent(newEventRequest);
        Category category = categoryRepository.getByIdAndCheck(newEventRequest.getCategory());
        LocalDateTime currentTime = LocalDateTime.now();
        validationHelper.isEventDateCorrect(event.getEventDate(), currentTime);

        if (newEventRequest.getPaid() == null) {
            event.setPaid(false);
        }
        if (newEventRequest.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (newEventRequest.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        event.setCategory(category);
        event.setCreatedOn(currentTime);
        event.setInitiator(user);
        event.setConfirmedRequests(0L);
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEvent(Long id, Long userId) {
        userRepository.getByIdAndCheck(userId);
        return EventMapper.toEventFullDto(eventRepository.getByIdAndCheck(id));
    }

    @Override
    public List<EventFullDto> getEventsByUser(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository
                .getByInitiatorId(userId, pageable)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateEventDto) {
        User user = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isUserNotOwnerOfEvent(user, event);
        validationHelper.isEventPublished(event);
        updateEventStatusByUser(updateEventDto, event);
        updateEventParams(updateEventDto, event);
        return EventMapper.toEventFullDto((eventRepository.save(event)));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventUpdateDto) {
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isEventPending(event);
        updateEventStatusByAdmin(eventUpdateDto, event);
        updateEventParams(eventUpdateDto, event);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public List<EventFullDto> getEventsByAdmin(List<Long> users,
                                               List<EventState> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Integer from,
                                               Integer size) {
        List<Event> events = eventRepository.getAllEventsByAdminByJpaBuddy(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size));
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().minusYears(1);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(1);
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Start is before end");
        }
        String sorting;
        if (sort.equals("EVENT_DATE")) {
            sorting = "eventDate";
        } else if (sort.equals("VIEWS")) {
            sorting = "views";
        } else {
            sorting = "id";
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sorting));
        List<Event> sortedEvents = eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, pageable);
        if (onlyAvailable) {
            sortedEvents.removeIf(event -> event.getParticipantLimit().equals(event.getConfirmedRequests()));
        }
        sortedEvents.forEach(e -> viewEvent(request, e));
        return eventRepository.saveAll(sortedEvents).stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventPublic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isEventNotPublished(event);
        viewEvent(request, event);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        User user = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        return requestRepository
                .getAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private void updateEventParams(UpdateEventUserRequest updateEventDto, Event event) {
        if (updateEventDto.getCategory() != null) {
            event.setCategory(categoryRepository.getByIdAndCheck(updateEventDto.getCategory()));
        }
        if (updateEventDto.getEventDate() != null) {
            validationHelper.isEventDateCorrect(updateEventDto.getEventDate(), LocalDateTime.now());
        }
        if (updateEventDto.getLocation() != null) {
            event.setLon(updateEventDto.getLocation().getLon());
            event.setLat(updateEventDto.getLocation().getLat());
        }
        Optional.ofNullable(updateEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventDto.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEventDto.getTitle()).ifPresent(event::setTitle);
    }

    private void viewEvent(HttpServletRequest request, Event event) {
        String uri = request.getRequestURI();
        sendStatistics(uri, request.getRemoteAddr(), "main");
        long views = getViewsFromStatistics(uri, event.getCreatedOn(), LocalDateTime.now());
        if (event.getViews() != views) {
            event.setViews(views);
        }
    }

    private long getViewsFromStatistics(String uri, LocalDateTime from, LocalDateTime to) {
        return Optional.ofNullable(statisticClient.getStatistic(from, to, List.of(uri), true))
                .map(ResponseEntity::getBody)
                .stream()
                .flatMap(Collection::stream)
                .filter(v -> v.getUri().equals(uri))
                .mapToLong(ViewStats::getHits)
                .sum();
    }

    private void sendStatistics(String uri, String ip, String app) {
        EndpointHit hit = new EndpointHit();
        hit.setIp(ip);
        hit.setUri(uri);
        hit.setApp(app);
        hit.setTimestamp(LocalDateTime.now());
        statisticClient.createStatistic(hit);
    }
}
