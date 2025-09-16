package ru.yandex.practicum.filmorate.controller;


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
    HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    Collection<User> getAll(){
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if ((user.getEmail() == null || user.getEmail().isBlank())  ) {
            throw new ConditionsNotMetException("User email cannot be empty");
        }

        if (!user.getEmail().contains("@")) throw new ConditionsNotMetException("Email must be symbol @");

        if ((user.getLogin() == null || user.getLogin().isBlank())  ) {
            throw new ConditionsNotMetException("Login cannot be empty");
        }

        if (user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }

        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ConditionsNotMetException("Birthday cannot be to future");
        }

        user.setId(getNewId());
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id must not be empty");
        }

        if(users.containsKey(newUser.getId())){
            User oldUser = users.get(newUser.getId());
            if ((newUser.getEmail() == null || newUser.getEmail().isBlank())  ) {
                throw new ConditionsNotMetException("User email cannot be empty");
            }

            if (!newUser.getEmail().contains("@")) throw new ConditionsNotMetException("Email must be symbol @");

            if ((newUser.getLogin() == null || newUser.getLogin().isBlank())  ) {
                throw new ConditionsNotMetException("Login cannot be empty");
            }

            if (newUser.getName() == null || newUser.getName().isBlank()){
                newUser.setName(newUser.getLogin());
            }

            if(newUser.getBirthday().isAfter(LocalDate.now())){
                throw new ConditionsNotMetException("Birthday cannot be to future");
            }

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;

        }
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
