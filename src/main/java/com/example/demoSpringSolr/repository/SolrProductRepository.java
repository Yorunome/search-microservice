package com.example.demoSpringSolr.repository;

import com.example.demoSpringSolr.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolrProductRepository extends SolrCrudRepository<Product, String> {

    Product findByProductId(String productid);

    //List<Product> findByCategoryId(String categoryid);

    void deleteByProductId(String productid);

    //todo : implement query for attributes and try out - finished




    //@Query("autoComplete:*?0*")
    @Query("Name:*?0* OR Attributes:*?0* OR USP:*?0* OR CategoryID:*?0*")
    List<Product> findByProductDescription(String searchTerm , Sort sort);

//    @Query("pdesc:*?0* OR pattr:*?0* OR pname:*?0*")
//    Page<Product> findByCustomerQuery(String searchTerm, Pageable pageable);


    //public Sort sortByWeight();

}
