package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    public static FilmController filmController = new FilmController();

    @AfterEach
    void afterEach() {
        filmController = new FilmController();
    }


    @DisplayName("Создание фильма с пустым названием")
    @Test
    void filmController_Create_NullNameAdd() {
        Film film = new Film();
        film.setId(0);
        film.setName("");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1910, 12, 28));
        film.setDuration(100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        }, "Поле name не может быть пустым");

        assertEquals(0, filmController.getAll().size(), "Фильм не должен был добавиться");
    }

    @DisplayName("Создание фильма с описанием длиной больше 200х символов")
    @Test
    void filmController_Create_DescriptionMore200Symbols() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(1910, 12, 28));
        film.setDuration(100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        }, "Поле description не должно быть больше 200х символов");

        assertEquals(0, filmController.getAll().size(), "Фильм не должен был добавиться");
    }

    @DisplayName("Создание фильма от даты больше заданной")
    @Test
    void filmController_Create_DataReleaseMoreTheSpecifiedOne() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1700, 12, 28));
        film.setDuration(100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        }, "Поле releaseDate не должно быть таким низким");

        assertEquals(0, filmController.getAll().size(), "Фильм не должен был добавиться");
    }

    @DisplayName("Создание фильма с отрицательной продолжительностью")
    @Test
    void filmController_Create_DurationIsNegative() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1900, 12, 28));
        film.setDuration(-100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.create(film);
        }, "Поле duration не должно быть таким негативным");

        assertEquals(0, filmController.getAll().size(), "Фильм не должен был добавиться");
    }

    @DisplayName("Обновление фильма пустым id")
    @Test
    void filmController_Update_IdIsNull() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1900, 12, 28));
        film.setDuration(100);

        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
        Film newFilm = new Film();
        newFilm.setId(null);
        newFilm.setName("test name");
        newFilm.setDescription("test description");
        newFilm.setReleaseDate(LocalDate.of(1900, 12, 28));
        newFilm.setDuration(100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.update(newFilm);
        }, "Поле id не должно быть пустым");
    }

    @DisplayName("Обновление фильма c несуществующим id")
    @Test
    void filmController_Update_IdIsNonExistent() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1900, 12, 28));
        film.setDuration(100);

        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
        Film newFilm = new Film();
        newFilm.setId(6498494);
        newFilm.setName("test name");
        newFilm.setDescription("test description");
        newFilm.setReleaseDate(LocalDate.of(1900, 12, 28));
        newFilm.setDuration(100);

        assertThrows(NotFoundException.class, () -> {
            filmController.update(newFilm);
        }, "Поле id должно быть в списке films");
    }

    @DisplayName("Обновление фильма c description больше 200 символов")
    @Test
    void filmController_Update_DescriptionMore200Symbols() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1900, 12, 28));
        film.setDuration(100);

        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("test name");
        newFilm.setDescription("test description".repeat(201));
        newFilm.setReleaseDate(LocalDate.of(1900, 12, 28));
        newFilm.setDuration(100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.update(newFilm);
        }, "Поле description должно быть меньше 200х символов");
    }

    @DisplayName("Обновление фильма c releaseDate старше минимальной даты")
    @Test
    void filmController_Update_ReleaseDateMoreMin() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1900, 12, 28));
        film.setDuration(100);

        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("test name");
        newFilm.setDescription("test description");
        newFilm.setReleaseDate(LocalDate.of(1700, 12, 28));
        newFilm.setDuration(100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.update(newFilm);
        }, "Поле releaseDate должно быть младше заданного минимума");
    }

    @DisplayName("Обновление фильма c отрицательным duration")
    @Test
    void filmController_Update_NegativeDuration() {
        Film film = new Film();
        film.setId(0);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(1900, 12, 28));
        film.setDuration(100);

        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("test name");
        newFilm.setDescription("test description");
        newFilm.setReleaseDate(LocalDate.of(2000, 12, 28));
        newFilm.setDuration(-100);

        assertThrows(ConditionsNotMetException.class, () -> {
            filmController.update(newFilm);
        }, "Поле duration должно быть положительным");
    }


}
