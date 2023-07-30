package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User edit(Long id, User user);

    List<User> getAll();

    User getById(Long id);

    void delete(Long id);
}
