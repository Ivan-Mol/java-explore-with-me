package ru.practicum.ewm.dto;

import lombok.Data;

@Data
public class EndpointHitDto {
    private String app;
    private String uri;
    private Long hits;
}
