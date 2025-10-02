package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film create(@Valid @RequestBody Film film);

    Film update(@Valid @RequestBody Film newFilm);

    Film remove(@Valid @RequestBody Film film);
}
