package com.readers.be3.config;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.response.BasicResponse;

@RestControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<BasicResponse> invalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BasicResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<BasicResponse> noSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.OK).body(new BasicResponse());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    protected BasicResponse multipartException(MultipartException e) {
        return new BasicResponse(e.getMessage());
    }

    // @ExceptionHandler({ReadersProjectException.class, InvalidInputException.class, Exception.class})
    // protected ResponseEntity<BasicResponse> handleException(RuntimeException e) {
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BasicResponse(e.getMessage()));
    // }

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // protected ResponseEntity<BasicResponse> handleException(MethodArgumentNotValidException e) {
    //     String message = e.getBindingResult().getFieldErrors().stream()
    //         .findFirst().map(fieldError ->
    //             String.format("%s 오류. %s", fieldError.getField(), fieldError.getDefaultMessage()))
    //         .orElse(e.getMessage());
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BasicResponse(message));
    // }
}
