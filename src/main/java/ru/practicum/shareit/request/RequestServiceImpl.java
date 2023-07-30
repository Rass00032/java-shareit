package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Request add(Long userId, Request request) {
        User user = getUserOrThrowException(userId);
        request.setCreated(LocalDateTime.now())
                .setRequester(user);
        request = requestRepository.save(request);
        return request;
    }

    @Override
    public Request getById(Long requestId, Long userId) {
        getUserOrThrowException(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с идентификатором: {0} не найден", requestId));
        List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requestId);
        request.setItems(items);
        return request;
    }

    @Override
    public List<Request> getAll(long userId, Pageable pageable) {
        List<Request> requests = requestRepository.findAllByRequesterIdNot(userId, pageable).toList();
        List<Long> requestIds = requests.stream().map(Request::getId).collect(Collectors.toList());
        Map<Long, List<Item>> items = itemRepository.findAllByRequestInOrderByIdAsc(requestIds).stream()
                .collect(Collectors.groupingBy(Item::getRequestId, Collectors.toList()));
        return requests.stream()
                .peek(request -> request.setItems(items.get(request.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Request> getAllForUser(Long userId, Pageable pageable) {
        User requester = getUserOrThrowException(userId);
        List<Request> requests = requestRepository.findAllByRequester(requester, pageable).toList();
        List<Long> requestIds = requests.stream().map(Request::getId).collect(Collectors.toList());
        Map<Long, List<Item>> items = itemRepository.findAllByRequestInOrderByIdAsc(requestIds).stream()
                .collect(Collectors.groupingBy(Item::getRequestId, Collectors.toList()));
        return requests.stream()
                .peek(request -> request.setItems(items.getOrDefault(request.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private User getUserOrThrowException(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Variables.USER_WITH_ID_NOT_FOUND, userId));
    }
}
