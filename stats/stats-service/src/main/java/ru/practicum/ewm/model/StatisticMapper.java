package ru.practicum.ewm.model;

import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.EndpointHitReturnDto;

public class StatisticMapper {

    public static Statistic statisticDtoToStatistic(EndpointHit endpointHit) {
        Statistic statistic = new Statistic();
        statistic.setApp(endpointHit.getApp());
        statistic.setUri(endpointHit.getUri());
        statistic.setIp(endpointHit.getIp());
        statistic.setTimestamp(endpointHit.getTimestamp());
        return statistic;
    }

    public static EndpointHitReturnDto statisticToStatisticDto(Statistic statistic) {
        EndpointHitReturnDto answerDTO = new EndpointHitReturnDto();
        answerDTO.setId(statistic.getId());
        answerDTO.setApp(statistic.getApp());
        answerDTO.setUri(statistic.getUri());
        answerDTO.setIp(statistic.getIp());
        answerDTO.setTimestamp(statistic.getTimestamp());
        return answerDTO;
    }

    public static ViewStats endpointHitToEndpointHitDto(ru.practicum.ewm.model.EndpointHit endpointHit) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(endpointHit.getApp());
        viewStats.setUri(endpointHit.getUri());
        viewStats.setHits(endpointHit.getHits());
        return viewStats;
    }
}
