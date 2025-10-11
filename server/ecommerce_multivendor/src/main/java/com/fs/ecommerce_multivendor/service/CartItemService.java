package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.entity.CartItems;

public interface CartItemService {

    CartItems updateCartItems(Long userId,Long id,CartItems cartItems);
    void removeCartItem(Long userId,Long cartItemId);
    CartItems findCartItemById(Long id);
}
