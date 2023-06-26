package ru.practicum.main.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    default Request getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Request with id: " + id + " is not found"));
    }

    List<Request> getAllByRequesterId(Long requesterId);

    List<Request> getByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> getAllByIdIn(List<Long> requestIds);

    List<Request> getAllByEventId(Long eventId);
}
