package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.Util;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final ItemRequestRepository repository;

    private final ItemRequestMapper mapper;

    private final ItemMapper itemMapper;

    @Transactional
    public ItemRequestDto addItemRequest(CreateItemRequest createRequest, long userId) {

        isUser("Не найден пользователь # при добовлении запроса на вещь!",userId);

        ItemRequest itemRequest = ItemRequest.builder()
                .description(createRequest.getDescription())
                .created(LocalDateTime.now())
                .requester(userId)
                .build();

        ItemRequest newItemRequest = repository.save(itemRequest);

        log.info("Добавлен запрос на вещь {}", newItemRequest);


        return mapper.toItemRequestDto(newItemRequest);
    }

    public Collection<ItemRequestDto> getItemsRequester(long userId) {

        isUser("Не найден пользователь # при запросе запращиваемых вещей!",userId);

        List<ItemRequest> itemRequestList = repository.findByRequester(userId);

        List<ItemRequestDto> itemRequestDtoList = mapper.toItemRequestDtoList(itemRequestList).stream()
                .map(itemRequestDto -> itemRequestDto.toBuilder()
                        .items(itemMapper.toItemDtoList(itemRepository.findByRequest(itemRequestDto.getId())))
                        .build())
                .collect(Collectors.toList());

        return itemRequestDtoList;
    }

    public Collection<ItemRequestDto> getItemsRequesterPagination(long userId, int from, int size) {

        if(!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    "Не найден пользователь # при запросе запращиваемых вещей в диапозоне от # до #!",userId,from,size);
        }

        Sort sortByDate = Sort.by(Sort.Direction.DESC,"created");
       // Pageable page = Util.validPageParam(from,size);
        Pageable page = PageRequest.of(from,size);
        List<ItemRequest> itemRequestList;

        if(repository.existsByRequester(userId)) {

            /*данный эндпоинт возвращает список запросов,
            на которые пользователь может ответить.
            Автор запроса не может ответить на свой же запрос*/
            /*List<ItemRequestDto> itemRequestDtoList = mapper.toItemRequestDtoList(repository.findAll(page).getContent());
            log.info("Получены все запросы на вещи в количестве {} в диапозоне от {} до {} без items",
                    itemRequestDtoList.size(),
                    from,
                    size
            );*/
            return new ArrayList<>();
        } else {

            List<ItemRequestDto> itemRequestDtoList = mapper.toItemRequestDtoList(repository.findAll(page).getContent()).stream()
                    .map(itemRequestDto -> itemRequestDto.toBuilder()
                            .items(itemMapper.toItemDtoList(itemRepository.findByRequest(itemRequestDto.getId())))
                            .build())
                    .collect(Collectors.toList());

            log.info("Получены все запросы на вещи в количестве {} в диапозоне от {} до {}",
                    itemRequestDtoList.size(),
                    from,
                    size
            );

            return itemRequestDtoList;
        }

    }


    public ItemRequestDto getItemRequestByIdForOtherUser(long userId, long requestId) {
        isUser("Не найден пользователь # при запросе заказа на вещь " + requestId, userId);

        ItemRequest itemRequest = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос на вещь под id = " + requestId));

        ItemRequestDto itemRequestDto = mapper.toItemRequestDto(itemRequest).toBuilder()
                .items(itemMapper.toItemDtoList(itemRepository.findByRequest(requestId)))
                .build();

        log.info("Получен заказ на вещь {}",itemRequestDto);

        return itemRequestDto;
    }

    private void isUser(String textException, long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException(textException,userId);
        }
    }
}
