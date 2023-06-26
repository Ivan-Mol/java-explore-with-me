package ru.practicum.main.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.exception.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    default Event getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Event with id: " + id + " is not found"));
    }

    List<Event> getByInitiatorId(Long userId, Pageable pageable);

    List<Event> getFirstByCategoryId(Long categoryId);

    @Query("select e from Event e " +
            "where e.initiator.id in :ids " +
            "and e.state in :states " +
            "and e.category.id in :categoriesIds " +
            "and e.eventDate between :eventDateStart and :eventDateEnd " +
            "order by e.id ASC")
    List<Event> getAllEventsByAdminByJpaBuddy(@Nullable List<Long> ids, @Nullable List<EventState> states, @Nullable List<Long> categoriesIds, @Nullable LocalDateTime eventDateStart, @Nullable LocalDateTime eventDateEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.state = :state " +
            "AND (:text IS NULL " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND ((coalesce(:rangeStart, :rangeEnd) IS NULL " +
            "AND e.eventDate > now()) " +
            "OR e.eventDate BETWEEN coalesce(:rangeStart, e.eventDate) " +
            "AND coalesce(:rangeEnd, e.eventDate))")
    List<Event> getEventsPublic(String text,
                                List<Long> categories,
                                Boolean paid,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                EventState state,
                                Pageable pageable);

    List<Event> getAllByIdIn(List<Long> ids);
}
