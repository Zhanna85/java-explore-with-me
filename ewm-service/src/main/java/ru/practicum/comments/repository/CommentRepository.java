package ru.practicum.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comments.model.Comment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByAuthorId(Long userId, PageRequest pageable);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByEventId(Long eventId);

    @Query(
            "select e " +
                    "from Comment AS e " +
                    "JOIN FETCH e.event " +
                    "JOIN FETCH e.author " +
                    "where (:author is null or e.author.id in :author) " +
                    "and (:event is null or e.event.id in :event)"
    )
    List<Comment> findAllByAuthorIdOrEventId(Long author, Long event, PageRequest pageable);

    List<Comment> findByEventIdIn(Set<Long> longs);
}