package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.StatSvc;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatSvc, Integer> {

    @Query(
            "select new ru.practicum.dto.ViewStats(st.app, st.uri, count(distinct st.ip))" +
            "from StatSvc as st " +
            "where st.timestamp between ?1 and ?2 " +
            "group by st.app, st.uri"
    )
    List<ViewStats> getStatsWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(
            "select new ru.practicum.dto.ViewStats(st.app, st.uri, count(st.ip))" +
            "from StatSvc as st " +
            "where st.timestamp between ?1 and ?2 " +
            "group by st.app, st.uri"
    )
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end);

    @Query(
            "select new ru.practicum.dto.ViewStats(st.app, st.uri, count(distinct st.ip))" +
            "from StatSvc as st " +
            "where st.timestamp between ?1 and ?2 " +
            "and st.uri in ?3 " +
            "group by st.app, st.uri"
    )
    List<ViewStats> getStatsByUrisListWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(
            "select new ru.practicum.dto.ViewStats(st.app, st.uri, count(st.ip))" +
            "from StatSvc as st " +
            "where st.timestamp between ?1 and ?2 " +
            "and st.uri in ?3 " +
            "group by st.app, st.uri"
    )
    List<ViewStats> getStatsByUrisList(LocalDateTime start, LocalDateTime end, List<String> uris);
}
