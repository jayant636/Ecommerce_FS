package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.dto.CreateProductRequest;
import com.fs.ecommerce_multivendor.entity.Product;
import com.fs.ecommerce_multivendor.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest req, Seller seller);
    public void deleteProduct(Long productId);
    public Product updateProduct(Long productId,Product product);
    Product findProductById(Long productId);
    List<Product> searchProduct(String query);
    public Page<Product> getAllProducts(String category,String brand,String colors,String sizes,Integer minPrice,Integer maxPrice,Integer minDiscount,String stock,Integer pageNumber,String sort);
    List<Product> getProductBySellerId(Long sellerId);


}
