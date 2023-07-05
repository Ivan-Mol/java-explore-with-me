package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.EndpointHitReturnDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    EndpointHitReturnDto createStatistic(EndpointHit endpointHit);

    List<ViewStats> getAllStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
