package ru.practicum.main.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "title", nullable = false, length = 120)
    String title;
    @Column(name = "annotation", nullable = false, length = 2000)
    String annotation;
    @Column(name = "description", nullable = false, length = 7000)
    String description;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    EventState state = EventState.PENDING;
    @CreationTimestamp
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    User initiator;
    @Column(name = "lat")
    Double lat;
    @Column(name = "lon")
    Double lon;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;
    @Column(name = "paid")
    Boolean paid;
    @Column(name = "moderation")
    Boolean requestModeration;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "confirmed_requests", nullable = false)
    Long confirmedRequests;
}