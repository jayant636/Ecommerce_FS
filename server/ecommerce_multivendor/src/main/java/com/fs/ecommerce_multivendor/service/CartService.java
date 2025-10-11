package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.entity.Cart;
import com.fs.ecommerce_multivendor.entity.CartItems;
import com.fs.ecommerce_multivendor.entity.Product;
import com.fs.ecommerce_multivendor.entity.User;

public interface CartService {

    public CartItems addCartItem(User user, Product product, String size,int quantity);
    public Cart findUserCart(User user);

}
