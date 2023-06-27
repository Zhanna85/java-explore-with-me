package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.PaginationSetup;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(
            "select e " +
                    "from Event AS e " +
                    "JOIN FETCH e.initiator " +
                    "JOIN FETCH e.category " +
                    "where e.initiator.id = :userId"
    )
    List<Event> findAllWithInitiatorByInitiatorId(Long userId, PaginationSetup paginationSetup);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);
}