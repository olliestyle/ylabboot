package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public List<UserBookResponse> getAll() {
        List<UserDto> list = userService.getAll();
        log.info("All users in getAll(): {}", list);
        return list.stream()
                .filter(Objects::nonNull)
                .map(userDto -> UserBookResponse
                        .builder()
                        .userId(userDto.getId())
                        .booksIdList(
                                bookService.getBooksByUserId(userDto.getId())
                                        .stream()
                                        .map(BookDto::getId)
                                        .collect(Collectors.toList())
                        )
                        .build())
                .collect(Collectors.toList());
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book update request in updateUserWithBooks(): {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request in updateUserWithBooks(): {}", userDto);
        UserDto toUpdate = userService.updateUser(userDto);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(toUpdate.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(toUpdate.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        UserDto userDto = userService.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id: " + userId + " not found");
        }
        log.info("User in getUserWithBooks(): {}", userDto);
        List<Long> booksId = bookService.getBooksByUserId(userDto.getId())
                .stream()
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("Collected bookIds in getUserWithBooks(): {}", booksId);
        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(booksId)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        List<BookDto> booksToDelete = bookService.getBooksByUserId(userId);
        log.info("Books to delete in deleteUserWithBooks: {}", booksToDelete);
        booksToDelete.forEach(bookDto ->  bookService.deleteBookById(bookDto.getId()));
        userService.deleteUserById(userId);
    }
}
