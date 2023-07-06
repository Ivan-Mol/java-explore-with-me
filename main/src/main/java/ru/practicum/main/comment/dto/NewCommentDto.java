package ru.practicum.main.comment.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class NewCommentDto {
    @NotBlank
    @Length(min = 3, max = 800)
    private String text;
}
