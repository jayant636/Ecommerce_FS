package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.dto.AuthResponse;
import com.fs.ecommerce_multivendor.dto.LoginRequest;
import com.fs.ecommerce_multivendor.dto.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest signupRequest);
    void sentLoginOtp(String email);
    AuthResponse signIn(LoginRequest loginRequest);

}
