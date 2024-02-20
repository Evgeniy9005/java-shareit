package ru.practicum.shareit.user;

public interface UserService {
    User addUser(User user);

    User upUser(User user);

    void deleteUser(User user);
}
