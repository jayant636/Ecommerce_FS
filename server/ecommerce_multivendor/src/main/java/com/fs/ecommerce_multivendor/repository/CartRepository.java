package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByUserId(Long id);


}
