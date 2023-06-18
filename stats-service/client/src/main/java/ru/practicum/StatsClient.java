package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.util.List;

@Service
@Slf4j
public class StatsClient {

    private final WebClient client;

    public StatsClient(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:9090").build();
    }

    public ResponseEntity<List<ViewStats>> getStats(String start, String end, List<String> uris, Boolean unique) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStats.class)
                .doOnNext(c -> log.info("Get stats with param: start date {}, end date {}, uris {}, unique {}",
                        start, end, uris, unique))
                .block();
    }

    public void saveStats(String app, String uri, String ip, String timestamp) {
        final EndpointHit endpointHit = new EndpointHit(app, uri, ip, timestamp);

        this.client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHit, EndpointHit.class)
                .retrieve()
                .toBodilessEntity()
                .doOnNext(c -> log.info("Save stats {}", endpointHit))
                .block();
    }
}