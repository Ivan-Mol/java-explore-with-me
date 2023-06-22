package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatisticDto;
import ru.practicum.ewm.dto.StatisticReturnDto;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Statistic;
import ru.practicum.ewm.model.StatisticMapper;
import ru.practicum.ewm.service.StatisticService;
import ru.practicum.ewm.storage.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository repository;

    @Override
    @Transactional
    public StatisticReturnDto createStatistic(StatisticDto statisticDto) {
        Statistic statistic = StatisticMapper.statisticDtoToStatistic(statisticDto);
        log.info("POST statisticDto {}", statisticDto);
        return StatisticMapper.statisticToStatisticDto(repository.save(statistic));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EndpointHitDto> getAllStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        dateCheck(start, end);
        if (uris.isEmpty()) {
            if (!unique) {
                log.info("GET start {} end {} unique = false", start, end);
                return repository.getAllStatistic(start, end)
                        .stream()
                        .map(StatisticMapper::endpointHitToEndpointHitDto)
                        .collect(Collectors.toList());
            }
            log.info("GET start {} end {} unique = true", start, end);
            return repository.getAllStatisticDist(start, end)
                    .stream()
                    .map(StatisticMapper::endpointHitToEndpointHitDto)
                    .collect(Collectors.toList());
        }
        if (!unique) {
            log.info("GET start {} end {} List<uris> {} unique = false", start, end, uris);
            return repository.getStatistic(start, end, uris)
                    .stream()
                    .map(StatisticMapper::endpointHitToEndpointHitDto)
                    .collect(Collectors.toList());
        }
        log.info("GET start {} end {} List<uris> {} unique = true", start, end, uris);
        return repository.getStatisticDistinct(start, end, uris)
                .stream()
                .map(StatisticMapper::endpointHitToEndpointHitDto)
                .collect(Collectors.toList());
    }

    private void dateCheck(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            log.info("End Is Before Start {} {}", start, end);
            throw new ValidationException("End is Before Start");
        }
    }

}
