package ru.practicum.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RequestServiceImplTest {

    private final Pageable pageable = Pageable.unpaged();
    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    private User requester;
    private Request request;
    private List<Request> requests;

    @BeforeEach
    void setUp() {
        User owner = User.builder()
                .id(1L)
                .name("John")
                .email("John-Smith@mail.com").build();
        requester = User.builder()
                .id(2L)
                .name("Angelina")
                .email("angelina@mail.com").build();
        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .id(1L)
                .owner(owner)
                .name("Grill")
                .description("Hot grill")
                .available(true).build());
        request = Request.builder()
                .id(1L)
                .description("I need hot grill")
                .requester(requester)
                .items(items)
                .build();
        requests = new ArrayList<>();
        requests.add(request);
    }

    @Test
    void createRequestWithValidInputsShouldReturnsRequest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(requester));
        when(requestRepository.save(any()))
                .then(invocation -> invocation.<Request>getArgument(0).setId(1L));

        Request result = requestService.add(1L, request);

        assertEquals(result.getId(), request.getId());
        assertEquals(result.getDescription(), request.getDescription());
        assertEquals(result.getRequester().getEmail(), request.getRequester().getEmail());
    }

    @Test
    void createRequestWithNonExistingUserShouldThrowsNotFoundException() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> requestService.add(3L, request)
        );

        assertEquals("User with ID: 3 not found", exception.getMessage());
    }

    @Test
    void getByIdWithValidInputsShouldReturnsRequest() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(requester));
        when(itemRepository.findAllByRequestInOrderByIdAsc(any()))
                .thenReturn(List.of(Item.builder().requestId(1L).build()));

        Request result = requestService.getById(1L, 1L);

        assertEquals(result.getId(), request.getId());
        assertEquals(result.getDescription(), request.getDescription());
    }

    @Test
    void getRequestByIdWithValidInputsShouldReturnsRequest() {
        when(userRepository.findById(requester.getId())).thenReturn(Optional.of(requester));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> requestService.getById(request.getId(), requester.getId())
        );

        assertEquals("Request with ID: 1 not found", exception.getMessage());
    }

    @Test
    void getAllForUserWithValidInputsShouldReturnsRequests() {
        Page<Request> requestsPage = new PageImpl<>(requests);
        when(userRepository.findById(requester.getId())).thenReturn(Optional.of(requester));
        when(requestRepository.findAllByRequester(any(), any())).thenReturn(requestsPage);
        when(itemRepository.findAllByRequestInOrderByIdAsc(any()))
                .thenReturn(List.of(Item.builder().requestId(1L).build()));

        List<Request> result = requestService.getAllForUser(2L, pageable);

        assertEquals(result.get(0).getId(), request.getId());
        assertEquals(result.get(0).getDescription(), request.getDescription());
    }

    @Test
    void getAllWithValidInputsShouldReturnsRequests() {
        Page<Request> requestsPage = new PageImpl<>(requests);
        when(requestRepository.findAllByRequesterIdNot(any(), any())).thenReturn(requestsPage);
        when(itemRepository.findAllByRequestInOrderByIdAsc(any()))
                .thenReturn(List.of(Item.builder().requestId(1L).build()));

        List<Request> result = requestService.getAll(2L, pageable);

        assertEquals(result.get(0).getId(), request.getId());
        assertEquals(result.get(0).getDescription(), request.getDescription());
    }
}