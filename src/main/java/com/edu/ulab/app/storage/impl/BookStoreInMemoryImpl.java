package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.storage.BookStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BookStoreInMemoryImpl implements BookStore {

    private final AtomicLong currentId = new AtomicLong(0);
    private final ConcurrentHashMap<Long, Book> storage = new ConcurrentHashMap<>();

    @Override
    public Book save(Book book) {
        book.setId(currentId.incrementAndGet());
        storage.put(currentId.get(), book);
        return book;
    }

    @Override
    public Collection<Book> getAll() {
        return storage.values();
    }

    @Override
    public Book getById(Long id) {
        return storage.get(id);
    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public List<Book> getBooksByUserId(Long id) {
        return storage.values()
                .stream()
                .filter(book -> id.equals(book.getUser().getId()))
                .collect(Collectors.toList());
    }
}
