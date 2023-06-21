package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatisticDto;
import ru.practicum.ewm.dto.StatisticReturnDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    StatisticReturnDto createStatistic(StatisticDto statisticDto);

    List<EndpointHitDto> getAllStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
