package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findAllByRequesterIdNot(Long requester, Pageable pageable);

    Page<Request> findAllByRequester(User requester, Pageable pageable);
}
