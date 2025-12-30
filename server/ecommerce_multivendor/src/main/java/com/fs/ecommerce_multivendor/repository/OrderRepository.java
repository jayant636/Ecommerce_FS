package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserId(Long userId);
    List<Order> findBySellerId(Long sellerId);


}
