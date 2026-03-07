package ru.yandex.practicum.exception;

public class ForbiddenStateChangeException extends RuntimeException {
    public ForbiddenStateChangeException(String message) {
        super(message);
    }
}