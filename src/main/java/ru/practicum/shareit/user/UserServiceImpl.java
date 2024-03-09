package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.patch.Patch;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {

        User user = userRepository.save(userMapper.toUser(userDto));

        UserDto addUserDto = userMapper.toUserDto(user);

        log.info("Добавлен пользователь {}", addUserDto);

        return addUserDto;
    }

    @Override
    public UserDto upUser(UserDto userDto, long userId) {

        User upUser = userMapper.toUser(
                Patch.patchUserDto(this.getUser(userId),userDto)
        );

        upUser.setId(userId);

        UserDto upUserDto = userMapper.toUserDto(userRepository.save(upUser));

        log.info("Обновлен пользователь {}", upUserDto);

        return upUserDto;
    }

    @Override
    public void deleteUser(long userId) {

        userRepository.deleteById(userId);
        log.info("Удален пользователь {}", userId);

    }

    @Override
    public UserDto getUser(long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NotFoundException("Не найден пользователь под id = "+userId)
        );

        log.info("Возвращается пользователь {}", user);

        return userMapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getUsers() {

        Sort sortById = Sort.by(Sort.Direction.ASC,"id");

        Collection<UserDto> set = userRepository.findAll(sortById).stream()
                .map(user -> userMapper.toUserDto(user))
                .collect(Collectors.toList());

        log.info("Возвращены все пользователи в количестве " + set.size());

        return set;
    }


}
