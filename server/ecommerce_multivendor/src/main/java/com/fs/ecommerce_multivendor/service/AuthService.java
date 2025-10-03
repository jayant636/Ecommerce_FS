package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.dto.AuthResponse;
import com.fs.ecommerce_multivendor.dto.LoginRequest;
import com.fs.ecommerce_multivendor.dto.SignupRequest;
import com.fs.ecommerce_multivendor.enums.USER_ROLE;

public interface AuthService {

    String createUser(SignupRequest signupRequest);
    void sentLoginOtp(String email, USER_ROLE role);
    AuthResponse signIn(LoginRequest loginRequest);

}
