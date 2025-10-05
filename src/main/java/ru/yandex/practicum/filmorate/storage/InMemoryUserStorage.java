package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    HashMap<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getById(Long id) {
        return Optional.ofNullable(users.get(id))
                .map(user -> {
                    log.info("id = {} was found user = {}", id, user.getName());
                    return user;
                })
                .orElseThrow(() -> {
                    log.warn("id = {} user not found", id);
                    return new NotFoundException("id = " + id + " user not found");
                });
    }

    @Override
    public User create(User user) {
        if ((user.getEmail() == null || user.getEmail().isBlank())) {
            log.error("User email cannot be empty");
            throw new ConditionsNotMetException("User email cannot be empty");
        }

        if (!user.getEmail().contains("@")) {
            log.error("Email must be symbol @");
            throw new ConditionsNotMetException("Email must be symbol @");
        }

        if ((user.getLogin() == null || user.getLogin().isBlank())) {
            log.error("Login cannot be empty");
            throw new ConditionsNotMetException("Login cannot be empty");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Name empty change to login {}", user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday cannot be to future");
            throw new ConditionsNotMetException("Birthday cannot be to future");
        }

        user.setId(getNewId());
        users.put(user.getId(), user);

        log.info("User create {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.error("Id must not be empty");
            throw new ConditionsNotMetException("Id must not be empty");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if ((newUser.getEmail() == null || newUser.getEmail().isBlank())) {
                log.error("User email cannot be empty to update");
                throw new ConditionsNotMetException("User email cannot be empty to update");
            }

            if (!newUser.getEmail().contains("@")) {
                log.error("Email must be symbol @ to update");
                throw new ConditionsNotMetException("Email must be symbol @");
            }

            if ((newUser.getLogin() == null || newUser.getLogin().isBlank())) {
                log.error("Login cannot be empty to update");
                throw new ConditionsNotMetException("Login cannot be empty to update");
            }

            if (newUser.getName() == null || newUser.getName().isBlank()) {
                log.info("Name is empty change to login to update {}", newUser.getLogin());
                newUser.setName(newUser.getLogin());
            }

            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.error("Birthday cannot be to future to update");
                throw new ConditionsNotMetException("Birthday cannot be to future to update");
            }

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());

            log.info("User update {}", oldUser);
            return oldUser;

        }
        log.error("User with id = {} not found", newUser.getId());
        throw new NotFoundException("User with id = " + newUser.getId() + " not found");
    }

    @Override
    public void remove(User user) {
        if (user.getId() == null) {
            log.error("Id must not be empty");
            throw new ConditionsNotMetException("Id must not be empty");
        }

        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            log.info("User remove {}", user);
        }

        log.error("User with id = {} not found", user.getId());
        throw new NotFoundException("User with id = " + user.getId() + " not found");
    }

    public Long getNewId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
