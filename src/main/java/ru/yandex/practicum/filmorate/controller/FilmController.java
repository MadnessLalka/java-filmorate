package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable Long id) {
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopPopularFilms(
            @RequestParam(defaultValue = "#{${filmorate.film.top.size:10}}") int count) {
        return filmService.getTopPopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping
    public void remove(@Valid @RequestBody Film film) {
        filmService.remove(film);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLikeFromFilm(id, userId);
    }
}
