package com.fs.ecommerce_multivendor.controller;

import com.fs.ecommerce_multivendor.dto.*;
import com.fs.ecommerce_multivendor.entity.VerificationCode;
import com.fs.ecommerce_multivendor.enums.USER_ROLE;
import com.fs.ecommerce_multivendor.repository.UserRepository;
import com.fs.ecommerce_multivendor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping(path = "/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest signupRequest){
        String jwt = authService.createUser(signupRequest);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("register success");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest loginOtpRequest) throws Exception{

       authService.sentLoginOtp(loginOtpRequest.getEmail(),loginOtpRequest.getRole());

        ApiResponse response = new ApiResponse();

        response.setMessage("otp sent successfully");

        return ResponseEntity.ok(response);

    }

    @PostMapping(path = "/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest) throws Exception{

        AuthResponse authResponse =  authService.signIn(loginRequest);
        return ResponseEntity.ok(authResponse);

    }
}
