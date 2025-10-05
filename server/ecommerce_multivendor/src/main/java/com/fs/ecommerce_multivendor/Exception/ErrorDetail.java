package com.fs.ecommerce_multivendor.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    private String message;
    private LocalDateTime localDateTime;
}
