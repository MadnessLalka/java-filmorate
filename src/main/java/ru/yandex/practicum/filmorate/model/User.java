package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of="id")
public class User {
    @NonNull
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDateTime birthday;

}
