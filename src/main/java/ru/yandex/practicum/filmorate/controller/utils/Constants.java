package ru.yandex.practicum.filmorate.controller.utils;

import java.time.LocalDate;

public class Constants {
    public static final int MAX_LENGTH_FILM_DESCRIPTION = 200;
    public static final LocalDate MIN_TIME_ADDING_FILM = LocalDate.of(1895, 12, 28);

    private Constants() {
    }
}