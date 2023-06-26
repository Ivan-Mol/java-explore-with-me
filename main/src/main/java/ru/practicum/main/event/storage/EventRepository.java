package ru.practicum.main.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.exception.exceptions.NotFoundException;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    default Event getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Event with id: " + id + " is not found"));
    }

    List<Event> getByInitiatorId(Long userId, Pageable pageable);
}
