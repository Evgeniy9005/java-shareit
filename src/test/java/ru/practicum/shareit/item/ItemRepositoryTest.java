package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {
    private final Map<Long, Set<Long>> userAndItems = new HashMap<>();
    @Test
    void test () {

      userAndItems.put(1L,new HashSet<>(Set.of(1l)));
      userAndItems.get(1l).add(2l);
        userAndItems.get(1l).add(2l);
        System.out.println(userAndItems);
    }
}