package ru.yandex.practicum.filmorate.exception;

public class DataIsEmptyException extends RuntimeException {
    public DataIsEmptyException(String message) {
        super(message);
    }
}
