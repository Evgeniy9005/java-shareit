package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.List;

public class Util {

    public static PageRequest validPageParam(Integer from, Integer size, int defaultFrom, int defaultSize, Sort sort) {
        if(from == null || size == null) {
            return PageRequest.of(defaultFrom,defaultSize,sort); //значит нужно задать from и size по умолчанию
        } else {
            if (from < 0 || from == size || size < 1) {
                throw new BadRequestException(
                        "Не верно заданы входные параметры для отображения данных в диапозоне от # до #  ", from, size);
            }

            return PageRequest.of(from,size,sort); //не по умолчанию
        }

    }


}
