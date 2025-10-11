package com.fs.ecommerce_multivendor.controller;

import com.fs.ecommerce_multivendor.dto.AddItemRequest;
import com.fs.ecommerce_multivendor.dto.ApiResponse;
import com.fs.ecommerce_multivendor.entity.Cart;
import com.fs.ecommerce_multivendor.entity.CartItems;
import com.fs.ecommerce_multivendor.entity.Product;
import com.fs.ecommerce_multivendor.entity.User;
import com.fs.ecommerce_multivendor.service.CartItemService;
import com.fs.ecommerce_multivendor.service.CartService;
import com.fs.ecommerce_multivendor.service.ProductService;
import com.fs.ecommerce_multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItems> addItemsToCart(@RequestBody AddItemRequest req,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());

        CartItems cartItems = cartService.addCartItem(user,product, req.getSize(), req.getQuantity());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Items added to the cart");

        return new ResponseEntity<>(cartItems,HttpStatus.CREATED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId , @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId() ,cartItemId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item remove from Cart");

        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItems> updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItems cartItems,@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        CartItems updateCartItem = null;
        if(cartItems.getQuantity() > 0){
            updateCartItem = cartItemService.updateCartItems(user.getId(),cartItemId,cartItems);
        }

        return new ResponseEntity<>(updateCartItem,HttpStatus.ACCEPTED);
    }

}
