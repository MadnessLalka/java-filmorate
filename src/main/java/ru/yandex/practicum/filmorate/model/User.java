package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private Long id;

    @NotNull
    @NotBlank
    @Email(message = "incorrect email address")
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;
    private LocalDate birthday;

    private Map<Long, Friendship> friends = new HashMap<>();

    public void setFriends(Long idFriend) {
        this.friends.put(idFriend, Friendship.UNCONFIRMED);
    }
}
