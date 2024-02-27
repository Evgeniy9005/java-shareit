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

    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {

        UserDto addUserDto = userMapper.toUserDto(userRepository.addUser(userMapper.toUser(userDto)));

        log.info("Добавлен пользователь {}", addUserDto);

        return addUserDto;
    }

    @Override
    public UserDto upUser(UserDto userDto, long userId) {

        UserDto upUserDto = userMapper.toUserDto(
                userRepository.upUser(
                        userMapper.toUser(userDto).toBuilder().id(userId).build()
                ));

        log.info("Обновлен пользователь {}", upUserDto);

        return upUserDto;
    }

    @Override
    public void deleteUser(long userId) {

        log.info("Удален пользователь {}", userId);

        userRepository.deleteUser(userId);
    }

    @Override
    public UserDto getUser(long userId) {

        log.info("Возвращается пользователь {}", userId);

        return userMapper.toUserDto(userRepository.getUser(userId));
    }

    @Override
    public Collection<UserDto> getUsers() {

        log.info("Возвращаются все пользователи");

        return userRepository.getUsers().stream()
                .map(user -> userMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

}
