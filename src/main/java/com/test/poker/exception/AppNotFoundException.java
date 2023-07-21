package com.test.poker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AppNotFoundException extends RuntimeException {
    private final String message;
}
