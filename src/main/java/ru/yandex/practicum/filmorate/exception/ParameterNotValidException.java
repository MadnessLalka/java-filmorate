package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ParameterNotValidException extends ValidationException {
    private final String parameter;
    private final String reason;
}
