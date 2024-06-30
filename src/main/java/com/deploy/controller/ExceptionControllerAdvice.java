package com.deploy.controller;

import com.deploy.dto.response.ErrorResponseBody;
import com.deploy.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return responseEntityWithValidation(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", e);
    }


    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException e) {
        String statusCode = e.getStatusCode();

        ErrorResponseBody errorResponseBody = ErrorResponseBody.builder()
                .code(statusCode)
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponseBody> response = ResponseEntity.status(Integer.valueOf(statusCode))
                .body(errorResponseBody);

        return response;
    }


    private ResponseEntity<?> responseEntityWithValidation(HttpStatus status, String message, MethodArgumentNotValidException e) {

        ErrorResponseBody errorResponseBody = ErrorResponseBody.builder()
                .code(String.valueOf(status.value()))
                .message(message)
                .build();

        for(FieldError fieldError : e.getFieldErrors()) {
            errorResponseBody.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errorResponseBody, new HttpHeaders(), status);
    }
}
