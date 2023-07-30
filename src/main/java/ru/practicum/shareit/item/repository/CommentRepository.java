package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.repository.model.Comment;
import ru.practicum.shareit.item.repository.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author WHERE comment.item IN ?1")
    List<Comment> findByItemIn(List<Item> items, Sort sorting);

    @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author WHERE comment.item.id = ?1")
    List<Comment> findByItemId(long itemId);
}
