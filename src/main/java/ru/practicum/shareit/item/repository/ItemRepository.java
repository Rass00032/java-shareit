package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(User user);

    @Query("SELECT item FROM Item item " +
            "WHERE (LOWER(item.name) LIKE CONCAT('%',?1,'%') " +
            "OR LOWER(item.description) LIKE CONCAT('%',?1,'%')) AND item.available=TRUE")
    List<Item> findLikingByNameOrDescription(String text);
}
