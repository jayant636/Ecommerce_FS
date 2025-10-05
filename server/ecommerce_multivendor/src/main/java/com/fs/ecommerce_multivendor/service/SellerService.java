package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.Exception.SellerException;
import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.enums.Accountstatus;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws SellerException;
    Seller createSeller(Seller seller);
    Seller getSellerById(Long id) throws Exception;
    Seller getSellerByEmail(String email) throws SellerException;
    List<Seller> getAllSellers();
    Seller updateSeller(Long id,Seller seller) throws Exception;


    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email,String otp) throws SellerException;
    Seller updateSellerAccountStatus(Long sellerId, Accountstatus status) throws Exception;

}
