package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    Request add(Long userId, Request request);

    Request getById(Long requestId, Long userId);

    List<Request> getAll(long userId, Pageable pageable);

    List<Request> getAllForUser(Long userId, Pageable pageable);
}
