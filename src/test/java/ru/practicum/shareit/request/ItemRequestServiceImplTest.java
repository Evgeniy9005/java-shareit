package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemMapperImpl;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    private ItemRequestService itemRequestService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private UserDto userDto1 = UserDto.builder()
            .id(1L)
            .name("user1")
            .email("user1@mail.ru")
            .build();


    private ItemRequest itemRequest1 = ItemRequest.builder()
            .id(1L)
            .description("Описание вещи для запроса")
            .requester(1L)
            .created(LocalDateTime.now())
            .build();

    private ItemRequestDto itemRequestDto1 = ItemRequestDto.builder()
            .id(1L)
            .description("Описание вещи для запроса")
            .requester(1L)
            .created(LocalDateTime.now())
            .build();


    @BeforeEach
    void start() {
    itemRequestService = new ItemRequestServiceImpl(userRepository,itemRepository,itemRequestRepository,new ItemRequestMapperImpl(),new ItemMapperImpl());

    }

    @Test
    void test1() throws Exception {
       // when(userService.addUser(any())).thenReturn(userDto1);

       // mvc.perform()


    }

    @Test
    void addItemRequestPagination() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        int from = 0;
        int size = 10;

        Pageable page = Util.validPageParam(from,size);

        Page<ItemRequest> pegs= new PageImpl<>(Data.generationData(10,ItemRequest.class));
        when(itemRequestRepository.findAll(page)).thenReturn(pegs);

        Collection<ItemRequestDto> itemRequestDtoList = itemRequestService.getItemsRequesterPagination(1,from,size);

        assertEquals(10,itemRequestDtoList.size());


        /*when(itemRequestService.addItemRequest(any(),anyLong()))
                .thenReturn(itemRequestDto1);


        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(itemRequestDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id",is(itemRequest1.getId()),Long.class))
        .andExpect(jsonPath("$.description",is(itemRequest1.getDescription())))
        .andExpect(jsonPath("$.requester",is(itemRequest1.getDescription()),Long.class));*/

    }


    @Test
    void getItemsRequester() {
    }

    @Test
    void getItemsRequesterPagination() {
    }

    @Test
    void getItemRequestByIdForOtherUser() {
    }
}