package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Integer id;

    @NotNull
    @NotBlank
    @Email(message = "incorrect email address")
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;
    private LocalDate birthday;

    private Set<Integer> friends;

}
