package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Collection<Film>> getAll() {

        Collection<Film> films = filmService.getAll();
        return ResponseEntity.ok(films);
    }

    @GetMapping("{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {

        Film film = filmService.getById(id);

        return ResponseEntity.ok(film);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getTopPopularFilms(
            @RequestParam(defaultValue = "#{${filmorate.film.top.size:10}}") int count
    ) {

        Collection<Film> films = filmService.getTopPopularFilms(count);

        return ResponseEntity.ok(films);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {

        Film newFilm = filmService.create(film);

        return ResponseEntity.status(HttpStatus.CREATED).body(newFilm);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film newFilm) {
        Film updatedFilm = filmService.update(newFilm);

        return ResponseEntity.ok(updatedFilm);
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<Void> addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(id, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@Valid @RequestBody Film film) {
        filmService.remove(film);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<Void> removeLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLikeFromFilm(id, userId);

        return ResponseEntity.ok().build();
    }
}
