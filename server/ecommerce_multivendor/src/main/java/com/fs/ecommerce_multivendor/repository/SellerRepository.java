package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.AccountStatus;
import com.fs.ecommerce_multivendor.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);

}
