package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.dto.CreateProductRequest;
import com.fs.ecommerce_multivendor.entity.Category;
import com.fs.ecommerce_multivendor.entity.Product;
import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.repository.CategoryRepository;
import com.fs.ecommerce_multivendor.repository.ProductRepository;
import com.fs.ecommerce_multivendor.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory());
        if(category1 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
        if(category2 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
        if(category3 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepository.save(category);
        }

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(),req.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);

        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice<=0) throw new IllegalArgumentException("Actual price must be greater than 0");
        double discount = mrpPrice - sellingPrice;
        int discountPercentage = (int) ((discount/mrpPrice)*100);
        return discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) {
       Product product = findProductById(productId);
       if (product == null) throw new RuntimeException("Product with this id doesn't exist"+productId);
       productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Product product1 = findProductById(productId);
        if (product == null) throw new RuntimeException("Product with this id doesn't exist"+productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product with this id Doesn't exists"+productId));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String color, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String stock, Integer pageNumber,String sort) {

//      Filtering
        Specification<Product> spec = (root,query,criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if(category != null){
                Join<Product,Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"),category));
            }

            if(color != null){

                predicates.add(criteriaBuilder.equal(root.get("color"),color));
            }

            if(sizes != null){
                predicates.add(criteriaBuilder.equal(root.get("sizes"),sizes));
            }

            if(minPrice != null){
                predicates.add(criteriaBuilder.equal(root.get("sellingPrice"),minPrice));
            }

            if(maxPrice != null){
                predicates.add(criteriaBuilder.equal(root.get("sellingPrice"),maxPrice));
            }

            if(stock != null){
                predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
            }

            if(minDiscount != null){
                predicates.add(criteriaBuilder.equal(root.get("discountPercent"),minDiscount));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

        Pageable pageable;
        if(sort != null && !sort.isEmpty()){
            pageable = switch (sort) {
                case "price_low" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
            };
        }else{
            pageable = PageRequest.of(pageNumber!=null ? pageNumber:0,10,Sort.unsorted());
        }

        return productRepository.findAll(spec,pageable);

    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
