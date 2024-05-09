package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {

    private final Long id;

    private final String name;

    private final String email;
}
