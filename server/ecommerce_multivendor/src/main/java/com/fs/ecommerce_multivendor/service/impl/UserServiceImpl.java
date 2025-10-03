package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.config.JwtProvider;
import com.fs.ecommerce_multivendor.entity.User;
import com.fs.ecommerce_multivendor.repository.UserRepository;
import com.fs.ecommerce_multivendor.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public UserServiceImpl(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = findUserByEmail(email);
        if(user == null) throw new Exception("User Not found with this email"+email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user == null) throw new Exception("User not found with this email"+email);
        return user;
    }
}
