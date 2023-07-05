package ru.practicum.main.category.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDto {
    Long id;
    @NotBlank
    @Length(max = 50)
    String name;
}
