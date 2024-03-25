package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static PageRequest validPageParam(int from, int size) {
            if (from < 0 || size < 1) {
                throw new BadRequestException(
                        "Не верно заданы входные параметры для отображения данных в диапозоне от # до #  ", from, size);
            }

        if(from > 1) {
            return PageRequest.of(from/size,size);
        }

        return PageRequest.of(from,size); //по умолчанию
    }

    public static PageRequest validPageParam(int from, int size, Sort sort) {
        if (from < 0 || size < 1) {
            throw new BadRequestException(
                    "Не верно заданы входные параметры для отображения данных в диапозоне от # до #  ", from, size);
        }

        if(from > 1) {
            return PageRequest.of(from/size,size,sort);
        }

        return PageRequest.of(from,size,sort); //по умолчанию
    }

    public static <T> List<T> getElementsFrom(List<T> list,int start){

        return list.stream().skip(start).collect(Collectors.toList());
    }
}
