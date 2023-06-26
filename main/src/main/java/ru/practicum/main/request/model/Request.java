package ru.practicum.main.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.event.model.Event;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    RequestStatus status = RequestStatus.PENDING;

    @Column(name = "created")
    LocalDateTime created;

}
