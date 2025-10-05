package com.fs.ecommerce_multivendor.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetail> sellerExceptionHandler(SellerException sellerException, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setMessage(sellerException.getMessage());
        errorDetail.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetail> productExceptionHandler(ProductException productException, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setMessage(productException.getMessage());
        errorDetail.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);

    }

}
