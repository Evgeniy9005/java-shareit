package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserDaoTest {

    @Autowired
    private UserRepository userDao;

    @Test
    void test(){

    }
}