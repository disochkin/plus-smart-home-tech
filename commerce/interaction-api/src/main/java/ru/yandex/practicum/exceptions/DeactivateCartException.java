package ru.yandex.practicum.exceptions;

public class DeactivateCartException extends RuntimeException {
    public DeactivateCartException(String message) {
        super(message);
    }
}
