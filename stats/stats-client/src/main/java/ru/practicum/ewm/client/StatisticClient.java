package ru.practicum.ewm.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatisticDto;
import ru.practicum.ewm.dto.StatisticReturnDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
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

    public StatisticReturnDto createStatistic(StatisticDto statisticDto) {
        return client
                .post()
                .uri("/hit")
                .body(statisticDto, StatisticDto.class)
                .retrieve()
                .bodyToMono(StatisticReturnDto.class)
                .block();
    }

    public ResponseEntity<List<EndpointHitDto>> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startDate = start.format(timeFormatter);
        String endDate = end.format(timeFormatter);
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", startDate)
                        .queryParam("end", endDate)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique.toString())
                        .build())
                .retrieve()
                .toEntityList(EndpointHitDto.class)
                .block();
    }
}

