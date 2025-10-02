package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

import static ru.yandex.practicum.filmorate.controller.utils.Constants.MAX_LENGTH_FILM_DESCRIPTION;
import static ru.yandex.practicum.filmorate.controller.utils.Constants.MIN_TIME_ADDING_FILM;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
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

    @Override
    public Film update(Film newFilm) {
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
    public Film remove(Film film) {
        if (film.getId() == null) {
            log.error("Id must not be empty");
            throw new ConditionsNotMetException("Id must not be empty");
        }

        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.info("Film remove {}", film);
        }

        log.error("Film with id = {} not found", film.getId());
        throw new NotFoundException("Film with id = " + film.getId() + " not found");
    }


    public Integer getNewId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
