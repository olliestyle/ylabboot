package com.edu.ulab.app.storage;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;

import java.util.List;

public interface BookStore extends Store<Book> {
    List<Book> getBooksByUserId(Long id);
}