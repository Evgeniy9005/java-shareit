package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.booking.IndicatorBooking;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {

        Item newItem = itemRepository.save(
                itemMapper.toItem(itemDto.toBuilder().owner(
                        userRepository.findById(userId).orElseThrow(
                                ()-> new NotFoundException(
                                        "при добовлении вещи не найден пользователь под id = " + userId))
                ).build())
        );

        log.info("Добавлена вещь {}",newItem);

        return itemMapper.toItemDto(newItem);
    }


    @Override
    public ItemDto upItem(ItemDto itemDto, long itemId, long userId) {

       // itemMapper.toItem(Patch.patchItemDto())

        if (userId <= 0) {
            throw new BadRequestException("Не коректный id пользователя = " + userId);
        }

        if (itemDto.getOwner().getId() != userId) {
            throw new NotFoundException("Не владелец этой вещи пользователь под id " + userId);
        }

        Item updateItem = itemRepository.save(itemMapper.toItem(itemDto));

        log.info("Обновлена вещь {}",updateItem);

        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new NotFoundException("Не найдена вещь под id = " + itemId));



        log.info("Вернулась вещь {}, пользователя {}", item, userId);

        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {

        log.info("Вернуть все вещи пользователя {}", userId);

        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> {
                    List<Booking> bookingsList = bookingRepository
                            .findByItemIdAndItemOwnerIdOrderByStartAsc(item.getId(),userId);

                    log.info("При возврате всех вещей пользователя. " +
                            "Бронирования предэдущий и следующий, всего штук {}!", bookingsList.size());

                    if(bookingsList.size() > 0) {
                        return itemMapper.toItemDto(item).toBuilder()
                                .lastBooking(new IndicatorBooking().getIndicatorBooking(bookingsList.get(0)))
                                .nextBooking(new IndicatorBooking().getIndicatorBooking(bookingsList.get(1)))
                                .build();
                    }
                return  itemMapper.toItemDto(item);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemByRequestUsers(long itemId, long userIdMakesRequest) {

        if (!userRepository.existsById(userIdMakesRequest)) {
           throw new NotFoundException(
                   "Не найден пользователь # при запросе вещи # пользователем #",
                   userIdMakesRequest,
                   itemId,
                   userIdMakesRequest
           );
        }


        Item item = itemRepository.findById(itemId).orElseThrow(
                ()-> new NotFoundException("Не найдена вешь под id = " + itemId)
        );

        List<Booking> bookingsList = bookingRepository
                .findByItemIdAndItemOwnerIdOrderByStartAsc(itemId,userIdMakesRequest);

        log.info("Бронирования предэдущий и следующий, всего штук {}", bookingsList.size());

        int size = bookingsList.size();

        List<Comment> comments = commentRepository.findByItemId(itemId);

        List<CommentDto> commentsDto = commentMapper.toCommentDtoList(comments);


        log.info("Вернулась вещь {} по запросу пользователя {}", item, userIdMakesRequest);

        return itemMapper.toItemDto(item).toBuilder()

                    .comments(commentsDto)
                    .build();

    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {

        log.info("Поиск вещей по тексту {}, по запросу пользователя {}", text, userId);

        if (text.isBlank()) {
        return new ArrayList<>(0);
        }

        return itemRepository.searchByIgnoreCaseDescriptionContainingAndAvailableTrue(text).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }


    public CommentDto addComment(CreateCommentDto createCommentDto, long itemId, long authorId) {

        Comment comment = Comment.builder()
                .text(createCommentDto.getText())
                .item(itemRepository.findById(itemId).orElseThrow(
                        ()-> new NotFoundException("Не найдена вещь #, при добовлении коментария",itemId)))
                .author(userRepository.findById(authorId).orElseThrow(
                        ()->new NotFoundException("Не найден пользователь #, при добовлении коментария",authorId)))
                .created(LocalDateTime.now())
                .build();

        Comment newComment = commentRepository.save(comment);

        log.info("Добавлен комментарий вещи {} пользователем {}!",itemId,authorId);

        return commentMapper.toCommentDto(newComment);
    }
}

