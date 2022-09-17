package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class UserStoreInMemoryImpl implements UserStore {

    private final AtomicLong currentId = new AtomicLong(0);
    private final ConcurrentHashMap<Long, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        user.setId(currentId.incrementAndGet());
        user.setBooks(new ArrayList<>());
        storage.put(currentId.get(), user);
        log.info("Create user with id: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return storage.values();
    }

    @Override
    public User getById(Long id) {
        return storage.get(id);
    }

    @Override
    public User update(User user) {
        if (!storage.containsKey(user.getId())) {
            save(user);
        } else {
            User toUpdate = storage.get(user.getId());
            toUpdate.setAge(user.getAge());
            toUpdate.setFullName(user.getFullName());
            toUpdate.setTitle(user.getTitle());
            log.info("User is updated: {}", toUpdate);
        }
        return user;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
