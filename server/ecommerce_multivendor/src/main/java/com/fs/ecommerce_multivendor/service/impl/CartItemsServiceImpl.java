package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.entity.CartItems;
import com.fs.ecommerce_multivendor.entity.User;
import com.fs.ecommerce_multivendor.repository.CartItemRepository;
import com.fs.ecommerce_multivendor.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItems updateCartItems(Long userId, Long id, CartItems cartItems) {
        CartItems cartItems1 = findCartItemById(id);

        User user = cartItems1.getCart().getUser();
        if(user.getId().equals(userId)){
            cartItems1.setQuantity(cartItems.getQuantity());
            cartItems1.setSellingPrice(cartItems1.getQuantity()*cartItems1.getProduct().getSellingPrice());
            cartItems1.setMrpPrice(cartItems1.getQuantity()*cartItems1.getProduct().getMrpPrice());
            return cartItemRepository.save(cartItems1);
         }
        throw new RuntimeException("Cart doesn't exist for this particular user"+userId);

    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
       CartItems cartItems = findCartItemById(cartItemId);
       User user = cartItems.getCart().getUser();
       if(user.getId().equals(userId)){
           cartItemRepository.delete(cartItems);
       }
      else{
          throw new RuntimeException("You can't delete this item:");
       }

    }

    @Override
    public CartItems findCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(()-> new RuntimeException("Cart item with this id not found"+id));
    }
}
