package com.fs.ecommerce_multivendor.controller;

import com.fs.ecommerce_multivendor.Exception.SellerException;
import com.fs.ecommerce_multivendor.config.JwtProvider;
import com.fs.ecommerce_multivendor.dto.AuthResponse;
import com.fs.ecommerce_multivendor.dto.LoginRequest;
import com.fs.ecommerce_multivendor.entity.AccountStatus;
import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.entity.SellerReports;
import com.fs.ecommerce_multivendor.entity.VerificationCode;
import com.fs.ecommerce_multivendor.repository.VerificationCodeRepository;
import com.fs.ecommerce_multivendor.service.AuthService;
import com.fs.ecommerce_multivendor.service.EmailService;
import com.fs.ecommerce_multivendor.service.SellerReportService;
import com.fs.ecommerce_multivendor.service.SellerService;
import com.fs.ecommerce_multivendor.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final JwtProvider jwtProvider;
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final SellerReportService sellerReportService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest loginRequest){
        String otp = loginRequest.getOtp();
        String email = loginRequest.getEmail();

        loginRequest.setEmail("seller_"+email);
        AuthResponse authResponse = authService.signIn(loginRequest);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception{
//       Making a repo call from controller is considered as bad practice
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new RuntimeException("Wrong otp ...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller seller1 = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOTP();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Ecommerce Vendor Email Verification Code";
        String text = "Welcome to Ecommerce Vendor , Verify your account using this link";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),subject,text+frontend_url);
        return new ResponseEntity<>(seller1,HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception{
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception{
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(){
        List<Seller> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,@RequestBody Seller seller) throws Exception{
        Seller seller1 = sellerService.getSellerProfile(jwt);
        Seller updateSeller = sellerService.updateSeller(seller1.getId(),seller);
        return ResponseEntity.ok(updateSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReports> getSellerReport(@RequestHeader("Authorization") String jwt) throws SellerException{
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReports reports = sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(reports,HttpStatus.OK);
    }

}
