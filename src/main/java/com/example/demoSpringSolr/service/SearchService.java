package com.example.demoSpringSolr.service;

import com.example.demoSpringSolr.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {

    List<Product> searchProduct(String term);
    void updateProduct(String productId, Double price);
    List<Product> findProduct(String productDesc);
    void deleteProduct(String productid);
    Page<Product> filterRatings(Double rating);
    Page<Product> filterAttributes(String attribute);
}
