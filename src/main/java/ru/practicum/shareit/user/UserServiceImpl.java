package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {

        return UserDto.toUserDto(userRepository.addUser(userDto.toUser()));
    }

    @Override
    public UserDto upUser(UserDto userDto, long userId) {
        User user = userDto.toUser().toBuilder().id(userId).build();
        return UserDto.toUserDto(userRepository.upUser(user));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public UserDto getUser(long userId) {
        return UserDto.toUserDto(userRepository.getUser(userId));
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userRepository.getUsers().stream()
                .map(user -> UserDto.toUserDto(user))
                .collect(Collectors.toList());
    }

}
