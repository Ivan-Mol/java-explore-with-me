package ru.practicum.main.comment.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.exception.exceptions.NotFoundException;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Comment with id: " + id + " is not found"));
    }

    List<Comment> getAllByAuthorId(Long authorId, Pageable pageable);

    List<Comment> getAllByEventId(Long eventId, Pageable pageable);

    Comment getByTextAndAuthorIdAndEventId(String text, Long authorId, Long eventId);

}