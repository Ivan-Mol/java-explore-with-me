package ru.practicum.main.comment.dto;


import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentResponseDto commentToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setAuthorId(comment.getAuthor().getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setCreatedOn(comment.getCreatedOn());
        commentResponseDto.setEventId(comment.getEvent().getId());
        return commentResponseDto;
    }

    public static Comment newCommentDtoToComment(NewCommentDto newCommentDto, User author, Event event) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        return comment;
    }

    public static Comment updatedCommentDtoToComment(UpdateCommentDto updateCommentDto, Comment oldComment) {
        Comment comment = new Comment();
        comment.setId(oldComment.getId());
        comment.setAuthor(oldComment.getAuthor());
        comment.setEvent(oldComment.getEvent());
        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
        } else {
            comment.setText(oldComment.getText());
        }
        comment.setCreatedOn(LocalDateTime.now());
        return comment;
    }

    public static CommentShortDto commentToCommentShortDto(Comment comment) {
        CommentShortDto commentShortDto = new CommentShortDto();
        commentShortDto.setAuthorName(comment.getAuthor().getName());
        commentShortDto.setText(comment.getText());
        commentShortDto.setCreatedOn(comment.getCreatedOn());
        return commentShortDto;
    }
}
