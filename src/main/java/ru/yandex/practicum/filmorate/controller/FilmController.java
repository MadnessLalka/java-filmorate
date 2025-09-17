package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

import static ru.yandex.practicum.filmorate.controller.Stubs.MAX_LENGTH_FILM_DESCRIPTION;
import static ru.yandex.practicum.filmorate.controller.Stubs.MIN_TIME_ADDING_FILM;

@RestController
@RequestMapping("/films")
public class FilmController implements IdGenerator {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Film title cannot be empty");
            throw new ConditionsNotMetException("Film title cannot be empty");
        }

        if (film.getDescription().length() > MAX_LENGTH_FILM_DESCRIPTION) {
            log.error("Max length of film description must no more " +
                    MAX_LENGTH_FILM_DESCRIPTION + " symbols");
            throw new ConditionsNotMetException("Max length of film description must no more " +
                    MAX_LENGTH_FILM_DESCRIPTION + " symbols");

        }

        if (film.getReleaseDate().isBefore(MIN_TIME_ADDING_FILM)) {
            log.error("Min realise date isn't before {}", MIN_TIME_ADDING_FILM);
            throw new ConditionsNotMetException("Min realise date isn't before " +
                    MIN_TIME_ADDING_FILM);
        }

        if (film.getDuration() <= 0) {
            log.error("Duration must be positive");
            throw new ConditionsNotMetException("Duration must be positive");
        }


        film.setId(getNewId());
        films.put(film.getId(), film);

        log.info("Film create {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id must not be empty");
            throw new ConditionsNotMetException("Id must not be empty");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                log.error("Film title cannot be empty to update");
                throw new ConditionsNotMetException("Film title cannot be empty to update");
            }

            if (newFilm.getDescription().length() > MAX_LENGTH_FILM_DESCRIPTION) {
                log.error("Max length of newFilm description must no more " +
                        MAX_LENGTH_FILM_DESCRIPTION + "symbols");
                throw new ConditionsNotMetException("Max length of newFilm description must no more " +
                        MAX_LENGTH_FILM_DESCRIPTION + "symbols");
            }

            if (newFilm.getReleaseDate().isBefore(MIN_TIME_ADDING_FILM)) {
                log.error("Min realise date isn't before {} to update", MIN_TIME_ADDING_FILM);
                throw new ConditionsNotMetException("Min realise date isn't before " +
                        MIN_TIME_ADDING_FILM + " to update");
            }

            if (newFilm.getDuration() <= 0) {
                log.error("Duration must be positive to update");
                throw new ConditionsNotMetException("Duration must be positive to update");
            }

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Film update {}", oldFilm);
            return oldFilm;
        }

        log.error("Film with id = {} not found", newFilm.getId());
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
