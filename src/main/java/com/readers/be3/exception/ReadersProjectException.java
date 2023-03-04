package com.readers.be3.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadersProjectException extends RuntimeException{

      private ErrorResponse errorResponse;


       @Override
       public String getMessage(){
        return String.format("%d  message: %s", errorResponse.getHttpStatus().value() ,errorResponse.getErrorMessage());
      }

      public HttpStatus getHttpStatus(){
        return this.errorResponse.getHttpStatus();
      }
  
}

