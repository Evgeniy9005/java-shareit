package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
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
import ru.practicum.shareit.util.Util;

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
    @Transactional
    public ItemDto addItem(ItemDto itemDto, long userId) {

        Item newItem = itemRepository.save(
                itemMapper.toItem(itemDto.toBuilder().owner(
                        userRepository.findById(userId).orElseThrow(
                                () -> new NotFoundException(
                                        "при добовлении вещи не найден пользователь под id = " + userId))
                ).build())
        );

        log.info("Добавлена вещь {}",newItem);

        return itemMapper.toItemDto(newItem);
    }


    @Override
    @Transactional
    public ItemDto upItem(ItemDto itemDto, long itemId, long userId) {

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
                .orElseThrow(() -> new NotFoundException("Не найдена вещь под id = " + itemId));

        log.info("Вернулась вещь {}, пользователя {}", item, userId);

        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId, int from, int size) {

        log.info("Вернуть все вещи пользователя {}", userId);

        Pageable page = Util.validPageParam(from,size);

        return Util.getElementsFrom(itemRepository.findByOwnerId(userId,page).stream()
                .map(item -> {
                    List<IndicatorBooking> indicatorBookingList = setIndicatorBooking(
                            bookingRepository.findByItemIdAndItemOwnerIdAndStatusOrderByStartAsc(
                                            item.getId(),userId,Status.APPROVED)
                    );

                    log.info("При возврате всех вещей пользователя. " +
                            "Бронирования предэдущий и следующий, всего штук {}!", indicatorBookingList.size());

                        return itemMapper.toItemDto(item).toBuilder()
                                .lastBooking(indicatorBookingList.get(0))
                                .nextBooking(indicatorBookingList.get(1))
                                .build();

                })
                .collect(Collectors.toList()),Util.start(from,size));
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
                () -> new NotFoundException("Не найдена вешь под id = " + itemId)
        );

        List<IndicatorBooking> indicatorBookingList = setIndicatorBooking(
                bookingRepository
                        .findByItemIdAndItemOwnerIdAndStatusOrderByStartAsc(itemId,userIdMakesRequest,Status.APPROVED)
        );

        log.info("Бронирования предэдущий и следующий, всего штук {}", indicatorBookingList.size());

        List<CommentDto> commentsDto = commentMapper.toCommentDtoList(
                commentRepository.findByItemId(itemId)
        );

        log.info("Вернулась вещь {} по запросу пользователя {}", item, userIdMakesRequest);

        return itemMapper.toItemDto(item).toBuilder()
                    .lastBooking(indicatorBookingList.get(0))
                    .nextBooking(indicatorBookingList.get(1))
                    .comments(commentsDto)
                    .build();

    }

    @Override
    public Collection<ItemDto> search(String text, long userId, int from, int size) {

        log.info("Поиск вещей по тексту {}, по запросу пользователя {}", text, userId);

        if (text.isBlank()) {
        return new ArrayList<>(0);
        }

        Pageable page = Util.validPageParam(from,size);

        return Util.getElementsFrom(
                itemRepository.searchByIgnoreCaseDescriptionContainingAndAvailableTrue(text,page).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toList()), Util.start(from,size)
        );
    }


    public CommentDto addComment(CreateCommentDto createCommentDto, long itemId, long authorId) {

        if (!bookingRepository
                .existsByItemIdAndBookerIdAndStatusAndEndBefore(itemId,authorId,Status.APPROVED,LocalDateTime.now())) {
          throw new BadRequestException("У пользователя небыло вещи # в аренде!",itemId);
        }

        Comment comment = Comment.builder()
                .text(createCommentDto.getText())
                .item(itemRepository.findById(itemId).orElseThrow(
                        () -> new NotFoundException("Не найдена вещь #, при добовлении коментария",itemId)))
                .author(userRepository.findById(authorId).orElseThrow(
                        () -> new NotFoundException("Не найден пользователь #, при добовлении коментария",authorId)))
                .created(LocalDateTime.now())
                .build();

        Comment newComment = commentRepository.save(comment);

        log.info("Добавлен комментарий вещи {} пользователем {}!",itemId,authorId);

        return commentMapper.toCommentDto(newComment);
    }

    private List<IndicatorBooking> setIndicatorBooking(List<Booking> bookingsList) {

        IndicatorBooking lastBooking = null;
        IndicatorBooking nextBooking = null;
        List<IndicatorBooking> indicatorBookingList = new ArrayList<>();

        if (bookingsList == null) {
            return indicatorBookingList;
        }

        int size = bookingsList.size();

        if (size == 1) {
            Booking bookingLast = bookingsList.get(0);
            lastBooking = new IndicatorBooking(bookingLast.getId(),bookingLast.getBooker().getId());
            indicatorBookingList.add(lastBooking);
            indicatorBookingList.add(nextBooking);
            return indicatorBookingList;
        }

        if (size == 2 || size == 3) {
            Booking bookingLast = bookingsList.get(0);
            Booking bookingNext = bookingsList.get(1);
            lastBooking = new IndicatorBooking(bookingLast.getId(),bookingLast.getBooker().getId());
            nextBooking = new IndicatorBooking(bookingNext.getId(),bookingNext.getBooker().getId());
            indicatorBookingList.add(lastBooking);
            indicatorBookingList.add(nextBooking);
            return indicatorBookingList;
        }

        if (size > 2) {
            Booking bookingLast = bookingsList.get(1);
            Booking bookingNext = bookingsList.get(size - 2);
            lastBooking = new IndicatorBooking(bookingLast.getId(),bookingLast.getBooker().getId());
            nextBooking = new IndicatorBooking(bookingNext.getId(),bookingNext.getBooker().getId());
            indicatorBookingList.add(lastBooking);
            indicatorBookingList.add(nextBooking);
            return indicatorBookingList;
        }

        indicatorBookingList.add(lastBooking);
        indicatorBookingList.add(nextBooking);
        return indicatorBookingList;
    }
}

