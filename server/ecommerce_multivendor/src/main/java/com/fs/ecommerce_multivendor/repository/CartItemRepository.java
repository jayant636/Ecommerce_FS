package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.Cart;
import com.fs.ecommerce_multivendor.entity.CartItems;
import com.fs.ecommerce_multivendor.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    CartItems findByCartAndProductAndSize(Cart cart, Product product,String size);
}
