package com.example.demoSpringSolr.repository;

import com.example.demoSpringSolr.entity.Product;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.data.solr.repository.Query;

import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolrOrderRepository extends SolrCrudRepository <Product, String>{

    Product findByProductId(String productid);

    List<Product> findByCategoryId(String categoryid);

    void deleteByProductId(String productid);

    @Query("autoComplete:*?0*")
    //todo : implement query for attributes and try out - finished

    List<Product> findByProductDescription(String searchTerm);

//    @Query("pdesc:*?0* OR pattr:*?0* OR pname:*?0*")
//    Page<Product> findByCustomerQuery(String searchTerm, Pageable pageable);




}



