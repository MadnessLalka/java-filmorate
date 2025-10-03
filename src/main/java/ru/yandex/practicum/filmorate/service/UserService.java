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

@Service
public class UserService implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> getUserFriends(Long id) {
        User user = inMemoryUserStorage.getById(id);

        log.trace("Getting list friends by {}", user);

        return user.getFriends().stream()
                .map(this::getById)
                .toList();
    }

    public Collection<User> getMutualFriends(Long id, Long otherId) {
        User currentUser = inMemoryUserStorage.getById(id);
        User comparableFriend = inMemoryUserStorage.getById(otherId);

        log.trace("Getting list mutual friends by {} & {}", currentUser.getEmail(), comparableFriend.getEmail());

        return currentUser.getFriends().stream()
                .filter(idFriend -> comparableFriend.getFriends().contains(idFriend))
                .map(inMemoryUserStorage::getById)
                .toList();
    }


    public void addToFriends(Long id, Long friendId) {
        User user = inMemoryUserStorage.getById(id);
        User newFriend = inMemoryUserStorage.getById(friendId);

        if (user.getFriends().contains(newFriend.getId())) {
            log.warn("User {} already to friend to {}", newFriend.getEmail(), user.getEmail());
            throw new DuplicateDataException("User + " + newFriend.getEmail()
                    + " already to friend to " + user.getEmail());
        }

        log.info("User {} was added as a friend {}", newFriend, user);

        user.setFriends(friendId);
        newFriend.setFriends(id);
    }

    public void removeFromFriends(Long id, Long friendId) {
        User user = inMemoryUserStorage.getById(id);
        User newFriend = inMemoryUserStorage.getById(friendId);

        if (user.getFriends().contains(newFriend.getId())) {
            log.info("User {} was removed from friend {}", newFriend.getEmail(), user.getEmail());

            user.getFriends().remove(newFriend.getId());
            newFriend.getFriends().remove(user.getId());
        }

        log.warn("User {} wasn't found to friends {}", newFriend.getEmail(), user.getEmail());
    }

    @Override
    public User getById(Long id) {
        return inMemoryUserStorage.getById(id);
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
    public void remove(User user) {
        inMemoryUserStorage.remove(user);
    }

}
