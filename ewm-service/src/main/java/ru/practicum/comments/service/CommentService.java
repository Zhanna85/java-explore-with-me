package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto saveComment(Long userId, Long eventId, NewCommentDto commentDto);

    List<CommentDto> getCommentsByUserId(Long userId, Integer from, Integer size);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto);

    CommentDto getCommentById(Long userId, Long commentId);

    void deleteCommentById(Long userId, Long commentId);

    List<CommentDto> getCommentsByEventId(Long userId, Long eventId);

    CommentDto updateCommentAdmin(Long commentId, NewCommentDto commentDto);

    CommentDto getCommentByIdAdmin(Long commentId);

    void deleteCommentByIdAdmin(Long commentId);

    List<CommentDto> getCommentsAdmin(Long userId, Long eventId, Integer from, Integer size);
}