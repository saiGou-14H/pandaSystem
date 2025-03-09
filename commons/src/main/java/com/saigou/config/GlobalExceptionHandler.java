package com.saigou.config;

import com.saigou.util.CustomException;
import com.saigou.util.ResponseEnum;
import com.saigou.util.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVO exception(Exception e){
        return ResponseVO.error(500,e.getMessage());
    }
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseVO exception(CustomException e){
        return ResponseVO.error(e.getCode(),e.getMessage());
    }
}
