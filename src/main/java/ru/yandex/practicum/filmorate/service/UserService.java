package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataIsEmptyException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        log.trace("Получение списка друзей у пользователя {}", user);
        return inMemoryUserStorage.getAll().stream()
                .filter(friendUser -> friendUser.getId().toString().contains(user.getFriends().toString()))
                .toList();
    }

    public Collection<User> getMutualFriends(User currentUser, User comparableFriend) {
        log.trace("Получение списка общих друзей {} и {}", currentUser.getEmail(), comparableFriend.getEmail());
        Collection<User> mutualFriends = getUserFriends(currentUser).stream()
                .filter((user) -> user.getId().toString().contains(comparableFriend.getFriends().toString()))
                .toList();

        if (mutualFriends.isEmpty()) {
            log.warn("У {} нет общий друзей с {}", currentUser.getEmail(), comparableFriend.getEmail());
            throw new DataIsEmptyException("У " + currentUser.getEmail() +
                    " нет общий друзей с " + comparableFriend.getEmail());
        }

        return mutualFriends;
    }


    public User addToFriends(User user, User newFriend) {
        if (!user.getFriends().toString().contains(newFriend.getId().toString())) {
            log.warn("Пользователь + {}уже находится в друзьях у {}", newFriend.getEmail(), user.getEmail());
            throw new DuplicateDataException("Пользователь + " + newFriend.getEmail()
                    + "уже находится в друзьях у " + user.getEmail());
        }

        log.debug("Пользователь {} был добавлен в друзья к {}", newFriend, user);

        user.setFriends(Collections.singleton(newFriend.getId()));

        return newFriend;
    }

    public User removeFromFriends(User user, User newFriend) {
        if (user.getFriends().toString().contains(newFriend.getId().toString())) {
            log.info("Пользователь {} бы удалён из друзей пользователя {}", newFriend.getEmail(), user.getEmail());
            user.getFriends().remove(newFriend.getId());
        }

        log.warn("Пользователь {}пока нет в друзьях у {}", newFriend.getEmail(), user.getEmail());
        throw new NotFoundException("Пользователь " + newFriend.getEmail() + "пока нет в друзьях у " + user.getEmail());
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
