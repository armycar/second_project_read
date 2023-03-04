package com.readers.be3.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private HttpStatus httpStatus;
  private String errorMessage;

  public static ErrorResponse of(HttpStatus httpStatus, String errorMessage){
    if (errorMessage == null){
      errorMessage = "";
    }
    return new ErrorResponse(httpStatus, errorMessage);
  }
}
