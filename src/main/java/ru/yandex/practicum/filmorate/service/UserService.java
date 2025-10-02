package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> getUserFriends(User user) {
        return inMemoryUserStorage.getAll().stream()
                .filter(friendUser -> friendUser.getId().toString().contains(user.getFriends().toString()))
                .toList();
    }

    public User addToFriends(User user, User newFriend) {
        if (!user.getFriends().toString().contains(newFriend.getId().toString())) {
            log.warn("Пользователь + {}уже находится в друзьях у {}", newFriend.getName(), user.getName());
            throw new DuplicateDataException("Пользователь + " + newFriend.getName()
                    + "уже находится в друзьях у " + user.getName());
        }

        log.debug("Пользователь {} был добавлен в друзья к {}", newFriend, user);
        user.setFriends(Collections.singleton(newFriend.getId()));
        return newFriend;
    }

    @Override
    public Collection<User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    @Override
    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    @Override
    public User update(User newUser) {
        return inMemoryUserStorage.update(newUser);
    }

    @Override
    public User remove(User user) {
        return inMemoryUserStorage.remove(user);
    }

    @Override
    public Integer getNewId() {
        return inMemoryUserStorage.getNewId();
    }
}
