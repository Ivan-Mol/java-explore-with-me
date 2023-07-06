package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentResponseDto;
import ru.practicum.main.comment.dto.CommentShortDto;
import ru.practicum.main.comment.dto.NewCommentDto;
import ru.practicum.main.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId);

    CommentResponseDto getCommentByAuthor(Long userId, Long commentId);

    CommentResponseDto getCommentByAdmin(Long commentId);

    CommentResponseDto updateCommentByAuthor(UpdateCommentDto updateCommentDto, Long userId, Long commentId);

    List<CommentResponseDto> getAllByAuthor(Long authorId, Integer from, Integer size);

    List<CommentShortDto> getAllForEvent(Long eventId, Integer from, Integer size);

    void deleteCommentByAuthor(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);
}
