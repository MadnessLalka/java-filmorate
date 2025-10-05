package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService implements FilmStorage {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLikeToFilm(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);

        if (film.getUserLikes().contains(user.getId())) {
            log.warn("User {} already liked this film {}", user.getEmail(), film.getName());
            throw new DuplicateDataException("User " + user.getEmail()
                    + " already liked this film " + film.getName());
        }

        log.info("User {} liked film {}", user.getEmail(), film.getName());
        film.setUserLikes(user.getId());
    }

    public void removeLikeFromFilm(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);

        if (film.getUserLikes().contains(user.getId())) {
            log.info("Removed like to this user {} from film {}", user.getEmail(), film.getName());
            film.getUserLikes().remove(user.getId());
        } else {
            log.warn("User {} wasn't found to liked film {}", user.getEmail(), film.getName());
        }

    }

    public Collection<Film> getTopPopularFilms(Integer filmTopSize) {
        if (filmStorage.getAll().isEmpty()) {
            log.warn("Film list is empty");
            throw new NotFoundException("Film list is empty");
        }

        log.trace("Getting list by top {} films by liked", filmTopSize);

        List<Film> topPopularFilmList = filmStorage.getAll().stream()
                .filter(film -> film.getUserLikes() != null && !film.getUserLikes().isEmpty())
                .sorted(Comparator.comparingLong((Film film) -> film.getUserLikes().size()).reversed())
                .limit(filmTopSize)
                .toList();

        if (topPopularFilmList.isEmpty()) {
            log.warn("Top film list is empty");
            throw new NotFoundException("Top film list is empty");
        }

        log.info("Top {} popular film is {}", filmTopSize, topPopularFilmList);
        return topPopularFilmList;

    }

    @Override
    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    @Override
    public void remove(Film film) {
        filmStorage.remove(film);
    }
}
