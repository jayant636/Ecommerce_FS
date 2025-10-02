package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.entity.User;
import com.fs.ecommerce_multivendor.enums.USER_ROLE;
import com.fs.ecommerce_multivendor.repository.SellerRepository;
import com.fs.ecommerce_multivendor.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImpl implements UserDetailsService {


    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private static final String SELLER_PREFIX = "seller_";


    public CustomUserServiceImpl(SellerRepository sellerRepository, UserRepository userRepository) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username.startsWith(SELLER_PREFIX)){
           String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername);

            if(seller != null){
                return buildUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());
            }
        }else{
            User user = userRepository.findByEmail(username);
            if(user != null){
                System.out.println(user+"user is null");
                return buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());
            }
        }

        throw new UsernameNotFoundException("User or Seller not found with this email"+username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if(role == null) role = USER_ROLE.ROLE_CUSTOMER;

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email,password,authorities
        );
    }
}
