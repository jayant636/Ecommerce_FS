package com.fs.ecommerce_multivendor.controller;

import com.fs.ecommerce_multivendor.Exception.SellerException;
import com.fs.ecommerce_multivendor.entity.Order;
import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.enums.OrderStatus;
import com.fs.ecommerce_multivendor.service.OrderService;
import com.fs.ecommerce_multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt) throws SellerException{
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellersOrder(seller.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(@RequestHeader ("Authorization") String jwt, @PathVariable Long orderId , @PathVariable OrderStatus orderStatus) throws Exception{
        Order order = orderService.updateOrderStatus(orderId,orderStatus);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

}
