package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService implements UserStorage {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> getUserFriends(Long id) {
        User user = inMemoryUserStorage.getById(id);

        log.trace("Getting list friends by {}", user);

        List<User> friendsUserList = user.getFriends().stream()
                .map(this::getById)
                .toList();

        if (friendsUserList.isEmpty()) {
            log.warn("User {} friends list is empty", user.getEmail());
            throw new NotFoundException("User " + user.getEmail() + "  friends list is empty");
        }

        log.info("Friends list of user {} is {}", user.getEmail(), friendsUserList);
        return friendsUserList;
    }

    public Collection<User> getMutualFriends(Long id, Long otherId) {
        User currentUser = inMemoryUserStorage.getById(id);
        User comparableFriend = inMemoryUserStorage.getById(otherId);

        log.trace("Getting list mutual friends by {} & {}", currentUser.getEmail(), comparableFriend.getEmail());

        List<User> mutualFriendsList = currentUser.getFriends().stream()
                .filter(idFriend -> comparableFriend.getFriends().contains(idFriend))
                .map(inMemoryUserStorage::getById)
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
        User user = inMemoryUserStorage.getById(id);
        User newFriend = inMemoryUserStorage.getById(friendId);

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
        User user = inMemoryUserStorage.getById(id);
        User newFriend = inMemoryUserStorage.getById(friendId);

        if (user.getFriends().contains(newFriend.getId())) {
            log.info("User {} was removed from friends {}", newFriend.getEmail(), user.getEmail());

            user.getFriends().remove(newFriend.getId());
            newFriend.getFriends().remove(user.getId());
        } else {
            log.warn("User {} wasn't found to friends {}", newFriend.getEmail(), user.getEmail());
        }
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
