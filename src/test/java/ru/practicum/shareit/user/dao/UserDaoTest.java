package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;


@DataJpaTest
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void test(){

    }
}