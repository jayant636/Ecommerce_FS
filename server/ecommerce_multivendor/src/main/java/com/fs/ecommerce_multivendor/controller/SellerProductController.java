package com.fs.ecommerce_multivendor.controller;

import com.fs.ecommerce_multivendor.Exception.ProductException;
import com.fs.ecommerce_multivendor.Exception.SellerException;
import com.fs.ecommerce_multivendor.dto.CreateProductRequest;
import com.fs.ecommerce_multivendor.entity.Product;
import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.service.ProductService;
import com.fs.ecommerce_multivendor.service.SellerService;
import com.fs.ecommerce_multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Product>> getProductBySellerId(
            @RequestHeader("Authorization") String jwt
    ) throws ProductException , SellerException{
        Seller seller = sellerService.getSellerProfile(jwt);

        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request , @RequestHeader("Authorization") String jwt) throws Exception{
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request,seller);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,@RequestBody Product product){
        try{
            Product product1 = productService.updateProduct(productId,product);
            return new ResponseEntity<>(product1,HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("Product not found with this productID"+productId+":"+e.getMessage());
        }
    }

}
