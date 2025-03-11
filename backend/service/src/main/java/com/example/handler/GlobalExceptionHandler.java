package com.example.handler;

import com.example.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler

    public Result exceptionHandler(Exception e){
        log.error("出现异常{}",e);
        return Result.error(e.getMessage());
    }
}
