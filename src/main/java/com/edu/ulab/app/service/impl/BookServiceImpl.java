package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStore;
import com.edu.ulab.app.storage.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookStore bookStore;
    private final UserStore userStore;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookStore bookStore, BookMapper bookMapper, UserStore userStore) {
        this.bookStore = bookStore;
        this.bookMapper = bookMapper;
        this.userStore = userStore;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        User user = userStore.getById(bookDto.getUserId());
        book.setUser(user);
        user.getBooks().add(book);
        return bookMapper.bookToBookDto(bookStore.save(book));
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        return null;
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.bookToBookDto(bookStore.getById(id));
    }

    @Override
    public void deleteBookById(Long id) {
        bookStore.deleteById(id);
    }

    @Override
    public List<BookDto> getAll() {
        return bookStore.getAll().stream()
                .map(bookMapper::bookToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBooksByUserId(Long id) {
        return bookStore.getBooksByUserId(id).stream()
                .map(bookMapper::bookToBookDto)
                .collect(Collectors.toList());
    }
}
