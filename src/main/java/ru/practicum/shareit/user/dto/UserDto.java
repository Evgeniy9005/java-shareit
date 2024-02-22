package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class UserDto {

    private final long id;

    private final String name;

    @Email
    @NotBlank
    private final String email;

    public User toUser() {

        return User.builder()
                .id(this.getId())
                .name(this.getName())
                .email(this.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
