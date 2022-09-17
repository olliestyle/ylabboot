package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.BookStore;
import com.edu.ulab.app.storage.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStore userStore;
    private final UserMapper userMapper;

    public UserServiceImpl(UserStore userStore, UserMapper userMapper) {
        this.userStore = userStore;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        log.info("User to create: {}", user);
        return userMapper.userToUserDto(userStore.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        log.info("User to update: {}", user);
        return userMapper.userToUserDto(userStore.update(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userStore.getById(id);
        log.info("User int getUserById(): {}", user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userStore.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userStore.getAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }
}
