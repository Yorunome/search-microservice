package com.example.demoSpringSolr.service;

import com.example.demoSpringSolr.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {

    public Page<Product> searchProducts(String term);
    public void updateProduct(String productId, Double price);
    public List<Product> findProduct(String productDesc);
    public void deleteProduct(String productid);
    public Page<Product> filterRatings(Double rating);
    public Page<Product> filterAttributes(String attribute);
}
