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
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.Util;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
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
                        .items(itemRepository.findByRequest(itemRequestDto.getId()))
                        .build())
                .collect(Collectors.toList());

        return itemRequestDtoList;
    }

    public Collection<ItemRequestDto> getItemsRequesterPagination(long userId, Integer from, Integer size) {

        if(!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    "Не найден пользователь # при запросе запращиваемых вещей в диапозоне от # до #!",userId,from,size);
        }

        Sort sortByDate = Sort.by(Sort.Direction.DESC,"created");
        Pageable page = Util.validPageParam(from,size,0,5,sortByDate);

        Page<ItemRequest> itemRequestPage = repository.findAll(page);

       // List<ItemRequest> itemRequestList = itemRequestPage.getContent().stream().map(itemRequest -> itemRequest);

        List<ItemRequestDto> itemRequestDtoList = mapper.toItemRequestDtoList(itemRequestPage.getContent()).stream()
                .map(itemRequestDto -> itemRequestDto.toBuilder()
                        .items(itemRepository.findByRequest(itemRequestDto.getId()))
                        .build())
                .collect(Collectors.toList());

        log.info("Получены все запросы на вещи в количестве {} в диапозоне от {} до {}",
                itemRequestDtoList.size(),
                from,
                size
        );

        return itemRequestDtoList;
    }


    private void isUser(String textException, long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException(textException,userId);
        }
    }
}
