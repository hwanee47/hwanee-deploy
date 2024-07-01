package com.deploy.controller;

import com.deploy.dto.response.ErrorRes;
import com.deploy.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

        ErrorRes errorRes = ErrorRes.builder()
                .code(statusCode)
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorRes> response = ResponseEntity.status(Integer.valueOf(statusCode))
                .body(errorRes);

        return response;
    }


    private ResponseEntity<?> responseEntityWithValidation(HttpStatus status, String message, MethodArgumentNotValidException e) {

        ErrorRes errorRes = ErrorRes.builder()
                .code(String.valueOf(status.value()))
                .message(message)
                .build();

        for(FieldError fieldError : e.getFieldErrors()) {
            errorRes.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errorRes, new HttpHeaders(), status);
    }
}
