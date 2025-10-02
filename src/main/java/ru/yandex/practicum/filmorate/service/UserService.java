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
        if (!inMemoryUserStorage.getAll().contains(user)) {
            log.warn("User {} not found to getting friends list", user);
            throw new NotFoundException("User " + user.getEmail() + " not found to getting friends list");
        }

        log.trace("Getting list friends by {}", user);
        return inMemoryUserStorage.getAll().stream()
                .filter(friendUser -> friendUser.getId().toString().contains(user.getFriends().toString()))
                .toList();
    }

    public Collection<User> getMutualFriends(User currentUser, User comparableFriend) {
        if (!inMemoryUserStorage.getAll().contains(currentUser)) {
            log.warn("User {} not found to getting mutual friends list", currentUser);
            throw new NotFoundException("User " + currentUser + " not found to getting mutual friends list");
        }

        if (!inMemoryUserStorage.getAll().contains(comparableFriend)) {
            log.warn("Friend {} not found", comparableFriend);
            throw new NotFoundException("Friend " + comparableFriend + " not found");
        }

        log.trace("Getting list mutual friends by {} & {}", currentUser.getEmail(), comparableFriend.getEmail());
        Collection<User> mutualFriends = getUserFriends(currentUser).stream()
                .filter((user) -> user.getId().toString().contains(comparableFriend.getFriends().toString()))
                .toList();

        if (mutualFriends.isEmpty()) {
            log.warn("{} hasn't mutual friend with {}", currentUser.getEmail(), comparableFriend.getEmail());
            throw new DataIsEmptyException(currentUser.getEmail() +
                    " hasn't mutual friend with " + comparableFriend.getEmail());
        }

        return mutualFriends;
    }


    public User addToFriends(User user, User newFriend) {
        if (!inMemoryUserStorage.getAll().contains(user)) {
            log.warn("User {} not found", user);
            throw new NotFoundException("User " + user + " not found");
        }

        if (!inMemoryUserStorage.getAll().contains(newFriend)) {
            log.warn("Friend {} not found", newFriend);
            throw new NotFoundException("Friend " + newFriend + " not found");
        }

        if (!user.getFriends().toString().contains(newFriend.getId().toString())) {
            log.warn("User {} already to friend to {}", newFriend.getEmail(), user.getEmail());
            throw new DuplicateDataException("User + " + newFriend.getEmail()
                    + " already to friend to " + user.getEmail());
        }

        log.info("User {} was added as a friend {}", newFriend, user);

        user.setFriends(Collections.singleton(newFriend.getId()));

        return newFriend;
    }

    public User removeFromFriends(User user, User newFriend) {
        if (!inMemoryUserStorage.getAll().contains(user)) {
            log.warn("User {} not found", user);
            throw new NotFoundException("User " + user + " not found");
        }

        if (!inMemoryUserStorage.getAll().contains(newFriend)) {
            log.warn("Friend {} not found", newFriend);
            throw new NotFoundException("Friend " + newFriend + " not found");
        }

        if (user.getFriends().toString().contains(newFriend.getId().toString())) {
            log.info("User {} was removed from friend {}", newFriend.getEmail(), user.getEmail());
            user.getFriends().remove(newFriend.getId());
            return newFriend;
        }

        log.warn("User {} wasn't found to friends {}", newFriend.getEmail(), user.getEmail());
        throw new NotFoundException("User " + newFriend.getEmail() + " wasn't found to friends " + user.getEmail());
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

}
