package ru.practicum.main.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.comment.dto.NewCommentDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.storage.CommentRepository;
import ru.practicum.main.event.dto.NewRequestUpdateDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.exception.exceptions.ConflictException;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.exception.exceptions.ValidationException;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestStatus;
import ru.practicum.main.request.storage.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidationHelper {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    public void isRequestAlreadyCreated(Long userId, Long eventId) {
        List<Request> requests = requestRepository.getByRequesterIdAndEventId(userId, eventId);
        if (!requests.isEmpty()) {
            throw new ConflictException("Request for event=" + eventId + " is already created");
        }
    }

    public void isEventPending(Event event) {
        if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Only pending events can be update");
        }
    }

    public void isParticipantLimitFull(Event event) {
        if (event.getParticipantLimit().equals(event.getConfirmedRequests()) && event.getParticipantLimit() > 0) {
            throw new ConflictException("Participant limit is Full");
        }
    }

    public void isUserOwnerOfEvent(User user, Event event) {
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException(
                    "User id=" + user.getId() + " is owner of event id=" + event.getId()
            );
        }
    }

    public void isUserNotOwnerOfEvent(User user, Event event) {
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ValidationException(
                    "User id=" + user.getId() + " is not owner of event id=" + event.getId()
            );
        }
    }

    public void isUserRequestOwner(User user, Request request) {
        if (!request.getRequester().equals(user)) {
            throw new ValidationException("User is not request owner");
        }
    }

    public void isEventDateCorrect(LocalDateTime eventDate, LocalDateTime localDateTime) {
        if (eventDate.minusHours(2).isBefore(localDateTime)) {
            throw new ValidationException("Date of event is not correct");
        }
    }

    public void isEventPublished(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event is already published");
        }
    }

    public void isEventNotPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event is not published");
        }
    }

    public void isEventAvailable(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event is not published");
        }
    }

    public void isRequestAuthorInitiator(Event event, User user) {
        if (event.getInitiator().equals(user)) {
            throw new ConflictException("Initiator is the author of request");
        }
    }

    public void isRequestAlreadyConfirmed(NewRequestUpdateDto newRequestUpdateDto, Request request) {
        if (request.getStatus().equals(RequestStatus.CONFIRMED) &&
                newRequestUpdateDto.getStatus().equals(RequestStatus.REJECTED)) {
            throw new ConflictException("Request is already Confirmed");
        }
    }

    public void isTitleToLong(String title) {
        if (title != null && title.length() > 50) {
            throw new ValidationException("Title is longer than 50");
        }
    }

    public void isUserAuthorOfComment(User user, Comment comment) {
        if (!comment.getAuthor().equals(user)) {
            throw new ValidationException("User is not comment author");
        }
    }

    public void isAuthorAlreadyHaveThisComment(User user, NewCommentDto newCommentDto, Long eventId) {
        if (commentRepository.getByTextAndAuthorIdAndEventId(newCommentDto.getText(), user.getId(), eventId) != null) {
            throw new ValidationException("User can not to create the same comment two times for one event");
        }
    }
}
