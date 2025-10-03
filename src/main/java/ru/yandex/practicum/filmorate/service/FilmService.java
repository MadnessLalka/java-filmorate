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
import java.util.Comparator;

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

    public void addLikeToFilm(Long id, Long userId) {
        Film film = inMemoryFilmStorage.getById(id);
        User user = inMemoryUserStorage.getById(userId);

        if (film.getUserLikes().contains(user.getId())) {
            log.warn("User {} already liked this film {}", user.getEmail(), film.getName());
            throw new DuplicateDataException("User " + user.getEmail()
                    + " already liked this film " + film.getName());
        }

        log.info("User {} liked film {}", user.getEmail(), film.getName());
        film.setUserLikes(user.getId());
    }

    public void removeLikeFromFilm(Long id, Long userId) {
        Film film = inMemoryFilmStorage.getById(id);
        User user = inMemoryUserStorage.getById(userId);

        if (film.getUserLikes().contains(user.getId())) {
            log.info("Removed like to this user {} from film {}", user.getEmail(), film.getName());
            film.getUserLikes().remove(user.getId());
        }

        log.warn("User {} wasn't found to liked film {}", user.getEmail(), film.getName());
    }

    public Collection<Film> getTopPopularFilms(Integer filmTopSize) {
        if (inMemoryFilmStorage.getAll().isEmpty()) {
            log.warn("Film list is empty");
            throw new NotFoundException("Film list is empty");
        }

        log.trace("Getting list by top {} films by liked", filmTopSize);
        return inMemoryFilmStorage.getAll().stream()
                .filter(film -> film.getUserLikes() != null && !film.getUserLikes().isEmpty())
                .sorted(Comparator.comparingLong((Film film) -> film.getUserLikes().size()).reversed())
                .limit(filmTopSize)
                .toList();
    }

    @Override
    public Film getById(Long id) {
        return inMemoryFilmStorage.getById(id);
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
    public void remove(Film film) {
        inMemoryFilmStorage.remove(film);
    }
}
