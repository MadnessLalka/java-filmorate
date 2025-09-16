package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;

import static ru.yandex.practicum.filmorate.controller.Stubs.MAX_LENGTH_FILM_DESCRIPTION;
import static ru.yandex.practicum.filmorate.controller.Stubs.MIN_TIME_ADDING_FILM;

@RestController
@RequestMapping("/films")
public class FilmController implements IdGenerator {

    HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Film title cannot be empty");
        }

        if (film.getDescription().length() > MAX_LENGTH_FILM_DESCRIPTION) {
            throw new MaxLengthException("Max length of film description must no more " +
                    MAX_LENGTH_FILM_DESCRIPTION + " symbols");
        }

        if (film.getReleaseDate().isBefore(MIN_TIME_ADDING_FILM)) {
            throw new RealiseDateException("Min realise date isn't before " +
                    MIN_TIME_ADDING_FILM);
        }

        if (film.getDuration() < 0) {
            throw new DurationException("Duration must be positive");
        }

        film.setId(getNewId());
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Id must not be empty");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                throw new ConditionsNotMetException("Film title cannot be empty");
            }

            if (newFilm.getDescription().length() > MAX_LENGTH_FILM_DESCRIPTION) {
                throw new MaxLengthException("Max length of newFilm description must no more " +
                        MAX_LENGTH_FILM_DESCRIPTION + "symbols");
            }

            if (newFilm.getReleaseDate().isBefore(MIN_TIME_ADDING_FILM)) {
                throw new RealiseDateException("Min realise date isn't before " +
                        MIN_TIME_ADDING_FILM);
            }

            if (newFilm.getDuration() < 0) {
                throw new DurationException("Duration must be positive");
            }

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }

        throw new NotFoundException("Film with id = " + newFilm.getId() + " not found");

    }

    @Override
    public Integer getNewId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
