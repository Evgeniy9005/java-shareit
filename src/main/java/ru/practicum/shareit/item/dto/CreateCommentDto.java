package ru.practicum.shareit.item.dto;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@RequiredArgsConstructor
public class CreateCommentDto {

    private final Long commentId;

    @NotBlank
    private final String text;

    /*public CreateCommentDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "CreateCommentDto{" +
                "text='" + text + '\'' +
                '}';
    }*/
}
