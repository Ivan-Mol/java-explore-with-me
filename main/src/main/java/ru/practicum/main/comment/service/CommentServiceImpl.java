package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CommentMapper;
import ru.practicum.main.comment.dto.CommentResponseDto;
import ru.practicum.main.comment.dto.CommentShortDto;
import ru.practicum.main.comment.dto.NewCommentDto;
import ru.practicum.main.comment.dto.UpdateCommentDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.storage.CommentRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserRepository;
import ru.practicum.main.utils.ValidationHelper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ValidationHelper validationHelper;

    @Override
    public CommentResponseDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId) {
        User author = userRepository.getByIdAndCheck(userId);
        Event event = eventRepository.getByIdAndCheck(eventId);
        validationHelper.isAuthorAlreadyHaveThisComment(author, newCommentDto, event.getId());
        Comment comment = CommentMapper.newCommentDtoToComment(newCommentDto, author, event);
        return CommentMapper.commentToCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto getCommentByAuthor(Long userId, Long commentId) {
        userRepository.getByIdAndCheck(userId);
        return CommentMapper.commentToCommentResponseDto(commentRepository.getByIdAndCheck(commentId));
    }

    @Override
    public CommentResponseDto updateCommentByAuthor(UpdateCommentDto updateCommentDto, Long userId, Long commentId) {
        User user = userRepository.getByIdAndCheck(userId);
        Comment oldComment = commentRepository.getByIdAndCheck(commentId);
        validationHelper.isUserAuthorOfComment(user, oldComment);
        Comment updatedComment = CommentMapper.updatedCommentDtoToComment(updateCommentDto, oldComment);
        return CommentMapper.commentToCommentResponseDto(commentRepository.save(updatedComment));
    }

    @Override
    public void deleteCommentByAuthor(Long userId, Long commentId) {
        User user = userRepository.getByIdAndCheck(userId);
        Comment comment = commentRepository.getByIdAndCheck(commentId);
        validationHelper.isUserAuthorOfComment(user, comment);
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        commentRepository.getByIdAndCheck(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponseDto getCommentByAdmin(Long commentId) {
        return CommentMapper.commentToCommentResponseDto(commentRepository.getByIdAndCheck(commentId));
    }

    @Override
    public List<CommentResponseDto> getAllByAuthor(Long authorId, Integer from, Integer size) {
        userRepository.getByIdAndCheck(authorId);
        Pageable pageable = PageRequest.of(from / size, size);
        return commentRepository
                .getAllByAuthorId(authorId, pageable)
                .stream()
                .map(CommentMapper::commentToCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentShortDto> getAllForEvent(Long eventId, Integer from, Integer size) {
        eventRepository.getByIdAndCheck(eventId);
        Pageable pageable = PageRequest.of(from / size, size);
        return commentRepository
                .getAllByEventId(eventId, pageable)
                .stream()
                .map(CommentMapper::commentToCommentShortDto)
                .collect(Collectors.toList());
    }

}
