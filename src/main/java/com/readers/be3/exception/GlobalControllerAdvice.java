package com.readers.be3.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(ReadersProjectException.class)
  public ResponseEntity<Object> applicationHandler(ReadersProjectException e){
    Map<String,Object> map = new HashMap<String,Object>();
    // log.error("error occur {}", e.toString());
    map.put("error", e.getMessage());
    return new ResponseEntity<Object>(map, e.getHttpStatus());
  }  
}
