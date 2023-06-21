package ru.practicum.ewm.model;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatisticDto;
import ru.practicum.ewm.dto.StatisticReturnDto;

public class StatisticMapper {

    public static Statistic StatisticDtoToStatistic(StatisticDto statisticDto) {
        Statistic statistic = new Statistic();
        statistic.setApp(statisticDto.getApp());
        statistic.setUri(statisticDto.getUri());
        statistic.setIp(statisticDto.getIp());
        statistic.setTimestamp(statisticDto.getTimestamp());
        return statistic;
    }

    public static StatisticReturnDto StatisticToStatisticDto(Statistic statistic) {
        StatisticReturnDto answerDTO = new StatisticReturnDto();
        answerDTO.setId(statistic.getId());
        answerDTO.setApp(statistic.getApp());
        answerDTO.setUri(statistic.getUri());
        answerDTO.setIp(statistic.getIp());
        answerDTO.setTimestamp(statistic.getTimestamp());
        return answerDTO;
    }

    public static EndpointHitDto EndpointHitToEndpointHitDto(EndpointHit endpointHit) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(endpointHit.getApp());
        endpointHitDto.setUri(endpointHit.getUri());
        endpointHitDto.setHits(endpointHit.getHits());
        return endpointHitDto;
    }
}
