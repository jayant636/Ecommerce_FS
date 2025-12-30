package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.entity.*;
import com.fs.ecommerce_multivendor.enums.OrderStatus;
import com.fs.ecommerce_multivendor.enums.Paymentstatus;
import com.fs.ecommerce_multivendor.repository.AddressRepoository;
import com.fs.ecommerce_multivendor.repository.OrderItemRepository;
import com.fs.ecommerce_multivendor.repository.OrderRepository;
import com.fs.ecommerce_multivendor.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    private final AddressRepoository addressRepoository;
    private final OrderRepository orderRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

//       checking shipping address already exists or not
        if(!user.getAddresses().contains(shippingAddress)){
            user.getAddresses().add(shippingAddress);
        }

//        save the new shipping address
        Address address = addressRepoository.save(shippingAddress);

//        brand 1 -> 4 shirts
//        brand 2 -> 3 pants
        Map<Long,List<CartItems>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

//        Set of orders
        Set<Order> order = new HashSet<>();



        for(Map.Entry<Long,List<CartItems>> entry : itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItems> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(CartItems::getSellingPrice).sum();
            int totalItem = items.stream().mapToInt(CartItems::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(Paymentstatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            order.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItems items1:items){
                OrderItem  orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(items1.getMrpPrice());
                orderItem.setProduct(items1.getProduct());
                orderItem.setQuantity(items1.getQuantity());
                orderItem.setSize(items1.getSize());
                orderItem.setUserId(items1.getUserId());
                orderItem.setSellingPrice(items1.getSellingPrice());

                savedOrder.getOrderItem().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }

        return order;
    }

    @Override
    public Order findOrderById(long id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order doesn't exists for this id"+id));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
       Order order = findOrderById(orderId);
       order.setOrderStatus(orderStatus);

       return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
        Order order = findOrderById(orderId);

        if(!user.getId().equals(order.getUser().getId())){
            throw new RuntimeException("You don't have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(()-> new RuntimeException("Order item doesn't exist for this id"+id));
    }

}
