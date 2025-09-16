package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController implements IdGenerator {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
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

    @PutMapping
    public User update(@RequestBody User newUser) {
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
        log.error("Film with id = {} not found", newUser.getId());
        throw new NotFoundException("Film with id = " + newUser.getId() + " not found");

    }

    @Override
    public Integer getNewId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
