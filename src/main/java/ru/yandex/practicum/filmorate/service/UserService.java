package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUserFriends(Long id) {
        User user = userStorage.getById(id);

        log.trace("Getting list friends by {}", user);

        List<User> friendsUserList = user.getFriends().stream()
                .map(userStorage::getById)
                .toList();

        if (friendsUserList.isEmpty()) {
            log.warn("User {} friends list is empty", user.getEmail());
        }

        log.info("Friends list of user {} is {}", user.getEmail(), friendsUserList);
        return friendsUserList;
    }

    public Collection<User> getMutualFriends(Long id, Long otherId) {
        User currentUser = userStorage.getById(id);
        User comparableFriend = userStorage.getById(otherId);

        log.trace("Getting list mutual friends by {} & {}", currentUser.getEmail(), comparableFriend.getEmail());

        List<User> mutualFriendsList = currentUser.getFriends().stream()
                .filter(idFriend -> comparableFriend.getFriends().contains(idFriend))
                .map(userStorage::getById)
                .toList();

        if (mutualFriendsList.isEmpty()) {
            log.warn("User {} mutual friends list with user {} is empty", currentUser.getEmail(), comparableFriend.getEmail());
            throw new NotFoundException("User " + currentUser.getEmail() +
                    "  mutual friends list with user " + comparableFriend.getEmail() + " is empty");
        }

        log.info("Mutual friends list of user {} is {}", currentUser.getEmail(), mutualFriendsList);
        return mutualFriendsList;
    }

    public void addToFriends(Long id, Long friendId) {
        User user = userStorage.getById(id);
        User newFriend = userStorage.getById(friendId);

        if (user.getFriends().contains(newFriend.getId())) {
            log.warn("User {} already to friends to {}", newFriend.getEmail(), user.getEmail());
            throw new DuplicateDataException("User + " + newFriend.getEmail()
                    + " already to friends to " + user.getEmail());
        }

        log.info("User {} was added as a friend {}", newFriend, user);

        user.setFriends(friendId);
        newFriend.setFriends(id);
    }

    public void removeFromFriends(Long id, Long friendId) {
        User user = userStorage.getById(id);
        User newFriend = userStorage.getById(friendId);

        if (user.getFriends().contains(newFriend.getId())) {
            log.info("User {} was removed from friends {}", newFriend.getEmail(), user.getEmail());

            user.getFriends().remove(newFriend.getId());
            newFriend.getFriends().remove(user.getId());
        } else {
            log.warn("User {} wasn't found to friends {}", newFriend.getEmail(), user.getEmail());
        }
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void remove(User user) {
        userStorage.remove(user);
    }
}
