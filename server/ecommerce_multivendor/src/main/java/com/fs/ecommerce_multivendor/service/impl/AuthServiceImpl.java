package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.config.JwtProvider;
import com.fs.ecommerce_multivendor.dto.AuthResponse;
import com.fs.ecommerce_multivendor.dto.LoginRequest;
import com.fs.ecommerce_multivendor.dto.SignupRequest;
import com.fs.ecommerce_multivendor.entity.Cart;
import com.fs.ecommerce_multivendor.entity.User;
import com.fs.ecommerce_multivendor.entity.VerificationCode;
import com.fs.ecommerce_multivendor.enums.USER_ROLE;
import com.fs.ecommerce_multivendor.repository.CartRepository;
import com.fs.ecommerce_multivendor.repository.UserRepository;
import com.fs.ecommerce_multivendor.repository.VerificationCodeRepository;
import com.fs.ecommerce_multivendor.service.AuthService;
import com.fs.ecommerce_multivendor.service.EmailService;
import com.fs.ecommerce_multivendor.utils.OtpUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service

public class AuthServiceImpl implements AuthService {
    public AuthServiceImpl(CustomUserServiceImpl customUserService, EmailService emailService, VerificationCodeRepository verificationCodeRepository, JwtProvider jwtProvider, CartRepository cartRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.customUserService = customUserService;
        this.emailService = emailService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.jwtProvider = jwtProvider;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    private final CustomUserServiceImpl customUserService;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final JwtProvider jwtProvider;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public String createUser(SignupRequest signupRequest) {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signupRequest.getEmail());

        if(verificationCode == null || !verificationCode.getOtp().equals(signupRequest.getOtp())){
            throw new RuntimeException("Wrong OTP...");
        }

        User user = userRepository.findByEmail(signupRequest.getEmail());
        if(user != null){
            throw new RuntimeException("User with this email already exists");
        }

        User newuser = new User();
        newuser.setEmail(signupRequest.getEmail());
        newuser.setFullName(signupRequest.getFullName());
        newuser.setRole(USER_ROLE.ROLE_CUSTOMER);
        newuser.setMobile("9811684141");
        newuser.setPassword(passwordEncoder.encode(signupRequest.getOtp()));

        user = userRepository.save(newuser);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(signupRequest.getEmail(),null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public void sentLoginOtp(String email) {
        String SIGNING_PREFIX = "signin_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(email);
            if (user==null){
                throw new RuntimeException("User not exist with provided email");
            }
        }

        VerificationCode isVerificationCodeExist = verificationCodeRepository.findByEmail(email);

        if(isVerificationCodeExist != null){
            verificationCodeRepository.delete(isVerificationCodeExist);
        }

        String otp = OtpUtil.generateOTP();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "ecommerce market login/signup otp";
        String text = "Your login/signup otp is -"+otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);

    }

    @Override
    public AuthResponse signIn(LoginRequest loginRequest) {
       String username = loginRequest.getEmail();
       String otp = loginRequest.getOtp();

       Authentication authentication = authenticate(username,otp);
       SecurityContextHolder.getContext().setAuthentication(authentication);

       String token = jwtProvider.generateToken(authentication);
       AuthResponse authResponse = new AuthResponse();
       authResponse.setJwt(token);
       authResponse.setMessage("Login Success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null :authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

       return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if(userDetails == null){
            throw new RuntimeException("Invalid username or password");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new RuntimeException("Wrong otp");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }
}
