package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidException;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Map<String, Long> emails = new HashMap<>();

    private int generationId = 0;

    public User addUser(User user) {

        String email = user.getEmail();

        this.valid(user.getId(), email);

        long id = ++generationId;
        emails.put(email,id);
        users.put(id,user.toBuilder().id(id).build());

        return users.get(id);
    }

    public User upUser(User user) {
        String name = user.getName();
        String email = user.getEmail();

        long id = user.getId();

        this.isUser(id);

        this.valid(id, email);

        User oldUser = users.get(id);

        if (name == null) {
            name = oldUser.getName();
        }

        if (email == null)  {
            email = oldUser.getEmail();
        }

        emails.remove(users.get(id).getEmail());//удалить старую почту
        emails.put(email,id);//добавить новую почту

        users.remove(id);
        users.put(id,user.toBuilder().name(name).email(email).build());

        return users.get(id);
    }

    public void deleteUser(long userId) {

        this.isUser(userId);

        emails.remove(users.get(userId).getEmail());//удалить почту

        users.remove(userId);
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUser(long userId) {

        this.isUser(userId);

        return users.get(userId);
    }

    public boolean isUser(long userId) {

        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь под id = %s не найден!",userId));
        }

        return true;
    }

    private void valid(long userId, String email) {
        if (emails.containsKey(email)) {
            if (emails.get(email) != userId) {
                throw new ValidException(String.format("Пользователь с таким адресом %s уже есть!",email));
            }
        }
    }
}
