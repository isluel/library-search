package com.library.config;

import com.library.ApiException;
import com.library.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Api Exception 에러
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        log.error("Api Exception occured. message={}, className={}", e.getErrorMessage(), e.getClass().getName());
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ErrorResponse(e.getErrorMessage(), e.getErrorType()));
    }

    // 나머지 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception occured. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorType.UNKNOWN.getDescription(), ErrorType.UNKNOWN));
    }

    // request의 valid 에러
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("Bind Exception occured. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(createMessage(e), ErrorType.INVALID_PARAMETER));
    }

    private String createMessage(BindException e) {
        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null) {
            return e.getFieldError().getDefaultMessage();
        }

        return e.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", ")) + " 값들이 정확하지 않습니다.";
    }
}
