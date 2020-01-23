package com.example.demoSpringSolr.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demoSpringSolr.dto.ProductDTO;
import com.example.demoSpringSolr.entity.Product;
import com.example.demoSpringSolr.repository.SolrOrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/Products")
public class SearchController {

    @Autowired
    SolrOrderRepository solrOrderRepository;

    @Autowired
    SolrTemplate solrTemplat;

    @PostMapping("/product")
    public String createOrder(@RequestBody Product product) {

        String description = "Product Created";

        Product product1 = solrOrderRepository.save(product);

        return description;

    }

    @GetMapping("/product/{productid}")

    public Product readProduct(@PathVariable String productid) {

        return solrOrderRepository.findByProductId(productid);

    }

    @GetMapping("/category/{categoryid}")

    public List <Product> readCateogry(@PathVariable String categoryid) {

        return solrOrderRepository.findByCategoryId(categoryid);

    }

    @GetMapping("/product/desc/{productDesc}/")

    public List <Product> findOrder(@PathVariable String productDesc){

        return solrOrderRepository.findByProductDescription(productDesc);

    }


    @DeleteMapping("/product/{productid}")

    public String deleteProduct(@PathVariable String productid) {

        String description = "Product Deleted";

        solrOrderRepository.deleteByProductId(productid);

        return description;

    }

    @GetMapping("/addProduct")
    @KafkaListener(topics = "Products", groupId = "group_id")
    public void consume(@RequestBody String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = new ProductDTO();
        try {
            productDTO = objectMapper.readValue(message, ProductDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<String> productAttributes = productDTO.getProductAttribute().entrySet().stream().map(entry -> entry.getKey() + " : " + entry.getValue()).collect(Collectors.toList());
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setProductAttribute(productAttributes);
        solrOrderRepository.save(product);
    }


    @GetMapping("/ratings/{rating}")
    public Page<Product> filterRatings(@PathVariable double rating) throws IOException, SolrServerException {

        Query query = new SimpleQuery(Criteria.where("Rating").is(rating));
        query.setRequestHandler("browser");
        return solrTemplat.query("Products_ver_8", query, Product.class);

//        String result = "Search done";
//        return results.getContent();

    }
    @GetMapping("/attribute/{attribute}")
    public Page<Product> filterAttributes(@PathVariable String attribute){
        Query query = new SimpleQuery((Criteria.where("Attributes").is(attribute)));
        query.setRequestHandler("browser");
        return solrTemplat.query("Products_ver_8", query, Product.class);
    }

    @GetMapping("/search/{term}")
    public Page<Product> searchProducts(@PathVariable String term){

        Query query = new SimpleQuery(Criteria.where("autoComplete").is(term));
        query.setRequestHandler("browser");
        return solrTemplat.query("Products_ver_8", query, Product.class);

    }

}
