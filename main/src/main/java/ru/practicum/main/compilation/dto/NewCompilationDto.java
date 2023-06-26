package ru.practicum.main.compilation.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewCompilationDto {
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
    private boolean pinned;
    private List<Long> events;
}
