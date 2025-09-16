package ru.yandex.practicum.filmorate.controller.exception;

public class MaxLengthException extends RuntimeException {
    public MaxLengthException(String message) {
        super(message);
    }
}
