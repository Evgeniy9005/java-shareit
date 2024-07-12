package ru.practicum.shareit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class Util {

    public static PageRequest createPageParam(int from, int size) {

        PageRequest pageRequest;
        if (from > 0) {
            pageRequest = PageRequest.of(from / size,size);
            log.info("{}",pageRequest);
            return pageRequest;
        }

        pageRequest = PageRequest.of(from,size);//по умолчанию
        log.info("{}",pageRequest);
        return PageRequest.of(from,size);
    }


    public static PageRequest createPageParam(int from, int size, Sort sort) {

        PageRequest pageRequest;
        if (from > 0) {
            pageRequest = PageRequest.of(from / size,size,sort);
            log.info("{}",pageRequest);
            return pageRequest;
        }
        pageRequest = PageRequest.of(from,size,sort); //по умолчанию
        log.info("{}",pageRequest);
        return pageRequest;
    }

    public static int start(int from, int size) {

        if (from > 0) {
            log.info("start {}",from  % size);
            return from  % size;
        }

        return 0;
    }

    public static <T> List<T> getElementsFrom(List<T> list,int start) {

        return list.stream().skip(start).collect(Collectors.toList());
    }
}
