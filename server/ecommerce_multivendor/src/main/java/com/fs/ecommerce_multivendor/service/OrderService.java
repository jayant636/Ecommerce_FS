package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.entity.*;
import com.fs.ecommerce_multivendor.enums.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(long id);
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Order cancelOrder(Long orderId,User user);
    OrderItem getOrderItemById(Long id);

}
