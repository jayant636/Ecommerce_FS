package com.fs.ecommerce_multivendor.dto;

import com.fs.ecommerce_multivendor.enums.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginOtpRequest {
   private String email;
   private String otp;
   private USER_ROLE role;
}
