package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.controller.utils.Constants.MAX_LENGTH_FILM_DESCRIPTION;

@Data
public class Film {
    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = MAX_LENGTH_FILM_DESCRIPTION,
            message = "Max length of film description must no more " +
                    MAX_LENGTH_FILM_DESCRIPTION + " symbols")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Duration must be positive")
    private int duration;
}
