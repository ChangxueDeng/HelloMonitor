package hello.monitor.server.controller;


import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import hello.monitor.server.entity.Result;
import hello.monitor.server.utils.StatusUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author ChangxueDeng
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public String validateExceptionHandler(ValidationException exception){
        log.warn("Resolved [{}:{}]", exception.getClass().getName(), exception.getMessage());
        return Result.failure(StatusUtils.STATUS_BAD_REQUEST, "参数错误").toJsonString();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validExceptionHandler(MethodArgumentNotValidException exception) {
        log.warn("Resolved [{}:{}]", exception.getClass().getName(), exception.getMessage());
        return Result.failure(StatusUtils.STATUS_BAD_REQUEST, "参数错误").toJsonString();
    }
}
