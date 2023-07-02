package ru.practicum.ewm;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.EndpointHitReturnDto;
import ru.practicum.ewm.service.StatisticService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class StatisticController {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatisticService service;

    @GetMapping("/stats")
    public List<ViewStats> getEndpointHit(@RequestParam(name = "start") @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                                          @RequestParam(name = "end") @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                                          @RequestParam(name = "uris", required = false, defaultValue = "") List<String> uris,
                                          @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("GET: start {} end {} List<uris> {} unique {}", start, end, uris, unique);
        return service.getAllStatistic(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitReturnDto createStatistic(@RequestBody @Valid EndpointHit endpointHit) {
        log.info("POST: statisticDto {}", endpointHit);
        return service.createStatistic(endpointHit);
    }
}
