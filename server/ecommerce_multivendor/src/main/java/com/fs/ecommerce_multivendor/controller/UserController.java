package com.fs.ecommerce_multivendor.controller;


import com.fs.ecommerce_multivendor.entity.User;
import com.fs.ecommerce_multivendor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader ("Authorization") String jwt) throws Exception {
      User user = userService.findUserByJwtToken(jwt);
      return ResponseEntity.ok(user);
    }
}
