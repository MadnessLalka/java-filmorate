package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public User getById(@PathVariable Long id){
        return userService.getById(id);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getUsersFriends(@PathVariable Long id){
        return userService.getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId){
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
       return userService.update(newUser);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId){
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId){
        userService.removeFromFriends(id, friendId);
    }

    @DeleteMapping
    public void remove(@Valid @RequestBody User user){
        userService.remove(user);
    }



}
