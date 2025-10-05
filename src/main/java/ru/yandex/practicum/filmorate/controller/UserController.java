package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAll() {
        Collection<User> users = userService.getAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<Collection<User>> getUsersFriends(@PathVariable Long id) {
        Collection<User> users = userService.getUserFriends(id);

        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        Collection<User> users = userService.getMutualFriends(id, otherId);

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User newUser = userService.create(user);

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(newUser);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User newUser) {
        User updatedUser = userService.update(newUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<Void> addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addToFriends(id, friendId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFromFriends(id, friendId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@Valid @RequestBody User user) {
        userService.remove(user);

        return ResponseEntity.ok().build();
    }
}
