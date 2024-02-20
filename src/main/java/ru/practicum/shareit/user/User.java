package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {

    private final int id;

    private final String name;
    //еще надо сделать проверку на дубликат email
    @Email
    @NotBlank
    private final String email;
}
