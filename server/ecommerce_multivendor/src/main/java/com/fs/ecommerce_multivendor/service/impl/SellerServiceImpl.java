package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.config.JwtProvider;
import com.fs.ecommerce_multivendor.entity.AccountStatus;
import com.fs.ecommerce_multivendor.entity.Address;
import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.enums.Accountstatus;
import com.fs.ecommerce_multivendor.enums.USER_ROLE;
import com.fs.ecommerce_multivendor.repository.AddressRepoository;
import com.fs.ecommerce_multivendor.repository.SellerRepository;
import com.fs.ecommerce_multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final AddressRepoository addressRepoository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) {
        Seller seller1 = sellerRepository.findByEmail(seller.getEmail());
        if(seller1 != null) throw new RuntimeException("Seller with this email id already exist"+seller.getEmail());

        Address address = addressRepoository.save(seller.getPickupAddress());
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(address);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());


        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws Exception {
        return sellerRepository.findById(id).orElseThrow(()-> new Exception("Seller Not Found with this id..."+id));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller == null) throw new RuntimeException("Seller with this email is not available"+email);
        return seller;
    }

    @Override
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller seller1 = getSellerById(id);
        if(seller.getSellerName() != null){
            seller1.setSellerName(seller.getSellerName());
        }

        if(seller.getMobile() != null){
            seller1.setMobile(seller.getMobile());
        }

        if(seller.getEmail() != null){
            seller1.setEmail(seller.getEmail());
        }

        if(seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null){
            seller1.getBusinessDetails().setBusinessName(
                    seller.getBusinessDetails().getBusinessName()
            );
        }

        if(seller.getBankDetails() != null && seller.getBankDetails().getAccountHoldername()!= null && seller.getBankDetails().getIfscCode()!= null && seller.getBankDetails().getAccountNumber() != null){
            seller1.getBankDetails().setAccountHoldername(seller.getBankDetails().getAccountHoldername());
            seller1.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            seller1.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
        }

        if(seller.getPickupAddress()!=null && seller.getPickupAddress().getAddress()!=null && seller.getPickupAddress().getCity()!=null && seller.getPickupAddress().getState()!=null && seller.getPickupAddress().getPinCode()!=null && seller.getPickupAddress().getMobile()!=null){
            seller1.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            seller1.getPickupAddress().setState(seller1.getPickupAddress().getState());
            seller1.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            seller1.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
            seller1.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
        }

        if(seller.getGSTIN() != null){
            seller1.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(seller1);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {

        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);

    }

    @Override
    public Seller verifyEmail(String email, String otp) {
       Seller seller = getSellerByEmail(email);
       seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, Accountstatus status) throws Exception {
       Seller seller = getSellerById(sellerId);
       seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
