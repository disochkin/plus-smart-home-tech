package ru.yandex.practicum.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiError {
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private String timestamp;
}