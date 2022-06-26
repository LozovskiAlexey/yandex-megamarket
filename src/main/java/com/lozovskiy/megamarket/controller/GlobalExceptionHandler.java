package com.lozovskiy.megamarket.controller;

import com.lozovskiy.megamarket.exception.ShopUnitImportInvalidPriceException;
import com.lozovskiy.megamarket.exception.ShopUnitNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(ShopUnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFountException(ShopUnitNotFoundException ex, WebRequest request) {
        Response bodyOfResponse = new Response(HttpStatus.NOT_FOUND.value(), "Item not found");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Response bodyOfResponse = new Response(HttpStatus.BAD_REQUEST.value(), "Validation Failed");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ShopUnitImportInvalidPriceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ShopUnitImportInvalidPriceException ex, WebRequest request) {
        Response bodyOfResponse = new Response(HttpStatus.BAD_REQUEST.value(), "Validation Failed");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Response bodyOfResponse = new Response(HttpStatus.BAD_REQUEST.value(), "Validation Failed");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        Response bodyOfResponse = new Response(HttpStatus.BAD_REQUEST.value(), "Validation Failed");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }

        return new ResponseEntity(body, headers, status);
    }

    @Getter
    @Setter
    static class Response {

        private Integer code;

        private String message;

        public Response(Integer code, String message){
            this.code = code;
            this.message = message;
        }
    }
}
