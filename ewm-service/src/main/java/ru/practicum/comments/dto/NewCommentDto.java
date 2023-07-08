package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class NewCommentDto {

    @NotBlank
    @Size(min = 2, max = 2000)
    private String text;
}