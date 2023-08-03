package ru.practicum;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Util {
    public static Pageable getPageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}
