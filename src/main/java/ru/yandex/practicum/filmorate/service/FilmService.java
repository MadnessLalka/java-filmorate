package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Collections;

@Service
public class FilmService implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    InMemoryFilmStorage inMemoryFilmStorage;
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addLikeToFilm(Film film, User user) {
        if (!inMemoryFilmStorage.getAll().contains(film)) {
            log.warn("Film {} not found", film);
            throw new NotFoundException("Film " + film.getName() + " not found");
        }

        if (!inMemoryUserStorage.getAll().contains(user)) {
            log.warn("User {} not found", user);
            throw new NotFoundException("User " + user.getEmail() + " not found");
        }

        if (!film.getUserLikes().toString().contains(user.getId().toString())) {
            log.warn("User {} already liked this film {}", user.getEmail(), film.getName());
            throw new DuplicateDataException("User " + user.getEmail()
                    + " already liked this film " + film.getName());
        }

        log.info("User {} liked film {}", user.getEmail(), film.getName());
        film.setUserLikes(Collections.singleton(user.getId()));
    }

    public void removeLikeFromFilm(Film film, User user) {
        if (!inMemoryFilmStorage.getAll().contains(film)) {
            log.warn("Film {} not found", film);
            throw new NotFoundException("Film " + film.getName() + " not found");
        }

        if (!inMemoryUserStorage.getAll().contains(user)) {
            log.warn("User {} not found", user);
            throw new NotFoundException("User " + user.getEmail() + " not found");
        }

        if (film.getUserLikes().toString().contains(user.getId().toString())) {
            log.info("Removed like to this user {} from film {}", user.getEmail(), film.getName());
            film.getUserLikes().remove(user.getId());
        }

        log.warn("User {} wasn't found to liked film {}", user.getEmail(), film.getName());
        throw new NotFoundException("User " + user.getEmail() + " wasn't found to liked film " + film.getName());
    }


    @Override
    public Collection<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    @Override
    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) {
        return inMemoryFilmStorage.update(newFilm);
    }

    @Override
    public Film remove(Film film) {
        return inMemoryFilmStorage.remove(film);
    }
}
