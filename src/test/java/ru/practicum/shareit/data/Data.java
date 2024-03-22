package ru.practicum.shareit.data;

import org.apache.el.stream.Stream;
import org.hibernate.mapping.Collection;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Data {


    public static <T> List<T> generationData(Integer createObjects, Type t, Object... objects) {

        return (List<T>) IntStream.iterate(1,i -> i+1)
                        .mapToObj(i -> getData(i,t,objects))
                        .limit(createObjects)
                        .collect(Collectors.toList());
    }

    private static <D> D getData(int i, Type type, Object... objects) {

        if(type.equals(User.class)) {
            return (D) new User(i,"User"+i,"user"+i+"@mail");
        }

        if(type.equals(ItemRequest.class)) {
            return (D) ItemRequest.builder()
                    .id(i)
                    .requester(1L)
                    .description("Запрос на вещь " + i)
                    .created(LocalDateTime.now())
                    .build();
        }

        if(type.equals(Item.class)) {
            if(objects.length == 2) {
                if (objects[0].getClass().equals(User.class) &&
                        (objects[1].getClass().equals(Long.class) || objects[1].getClass().equals(Integer.class))) {

                    return (D) new Item(i, "item" + i, "описание вещи " + i, false, (User) objects[0],(long)objects[1]);
                }
            }
           return (D) new Item(i,"item"+i,"описание вещи "+i, false, new User(1,"User","user@mail"),1L);
        }

        return null;
    }


    /*private Class<?> getGenericClass() {
        Class<?> result = null;
        Type type = this.getClass().getGenericSuperclass();

        if(type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] fieldArgTypes = pt.getActualTypeArguments();
            result = (Class<?>) fieldArgTypes[0];
        }

        return result;
    }*/

    public static <T> void printList(List<T> list,String separator){
        System.out.println(separator.repeat(100));
        list.stream().forEach(System.out::println);
        System.out.println(separator.repeat(100));
    }

    public static void separato__________________________________r() {
        System.out.println("***************************************************************");
    }
}
