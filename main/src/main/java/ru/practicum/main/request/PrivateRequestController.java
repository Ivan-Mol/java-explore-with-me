package ru.practicum.main.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.info("POST /users/userId = {}/requests: eventId = {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequestsByUser(@PathVariable("userId") Long userId) {
        return requestService.getRequestsByUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}