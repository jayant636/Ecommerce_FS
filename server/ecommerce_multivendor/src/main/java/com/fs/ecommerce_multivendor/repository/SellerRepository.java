package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    Seller findByEmail(String email);

}
