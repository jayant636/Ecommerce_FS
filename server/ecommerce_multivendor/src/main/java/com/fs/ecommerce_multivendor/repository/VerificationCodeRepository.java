package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    VerificationCode findByEmail(String email);

}
