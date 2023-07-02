package ru.practicum.ewm.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.EndpointHitReturnDto;
import ru.practicum.ewm.dto.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@Slf4j
public class StatisticClient {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final WebClient client;
    @Value("${client.url:http://localhost:9090}")
    private String url;
    public StatisticClient() {
        this.client = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public EndpointHitReturnDto createStatistic(EndpointHit endpointHit) {
        return client
                .post()
                .uri("/hit")
                .body(endpointHit, EndpointHit.class)
                .retrieve()
                .bodyToMono(EndpointHitReturnDto.class)
                .block();
    }

    public ResponseEntity<List<ViewStats>> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startDate = start.format(timeFormatter);
        String endDate = end.format(timeFormatter);
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", startDate)
                        .queryParam("end", endDate)
                        .queryParam("uris", String.join(",", uris))
                        .queryParam("unique", unique.toString())
                        .build())
                .retrieve()
                .toEntityList(ViewStats.class)
                .block();
    }
}

