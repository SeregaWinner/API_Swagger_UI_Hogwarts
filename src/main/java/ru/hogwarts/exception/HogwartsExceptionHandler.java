package ru.hogwarts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HogwartsExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> hendleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }@ExceptionHandler(AvatarProcessingException.class)
    public ResponseEntity<String> hendleAvatarProcessingException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось получить аватар из запроса или из файла");
    }

}
