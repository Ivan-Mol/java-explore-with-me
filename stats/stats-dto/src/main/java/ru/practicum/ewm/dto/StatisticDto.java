package ru.practicum.ewm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class StatisticDto {
    Long id;
    @NotNull(message = "App is null")
    @NotEmpty(message = "App is empty")
    private String app;
    @NotNull(message = "uri is null")
    @NotEmpty(message = "uri is empty")
    private String uri;
    @NotNull(message = "ip is null")
    @NotEmpty(message = "ip is empty")
    private String ip;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
