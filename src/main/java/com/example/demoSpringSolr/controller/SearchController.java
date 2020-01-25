package com.example.demoSpringSolr.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demoSpringSolr.dto.PriceUpdateDTO;
import com.example.demoSpringSolr.dto.ProductDTO;
import com.example.demoSpringSolr.dto.SearchDTO;
import com.example.demoSpringSolr.entity.Product;
import com.example.demoSpringSolr.repository.SolrProductRepository;
import com.example.demoSpringSolr.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/Products")
public class SearchController {

    @Autowired
    private SolrProductRepository solrProductRepository;

    @Autowired
    private SolrTemplate solrTemplat;

    @Autowired
    private SearchService searchService;

    @PostMapping("/product")
    public String createOrder(@RequestBody Product product) {

        String description = "Product Created";

        Product product1 = solrProductRepository.save(product);

        return description;

    }
//
//    @GetMapping("/product/{productid}")
//
//    public Product readProduct(@PathVariable String productid) {
//
//        return solrProductRepository.findByProductId(productid);
//
//    }


    @PostMapping("/product/desc/")

    public List <Product> findProduct(@RequestBody SearchDTO searchDTO){

        String searchTerm = searchDTO.getTerm();
        return searchService.findProduct(searchTerm);

    }


    @DeleteMapping("/product/{productid}")

    public String deleteProduct(@PathVariable String productid) {

        String description = "Product Deleted";
        searchService.deleteProduct(productid);
        return description;

    }

//    @PostMapping("/addProduct")
//    @KafkaListener(topics = "Products", groupId = "group_id")
//    public void consume(@RequestBody String message) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ProductDTO productDTO = new ProductDTO();
//        try {
//            productDTO = objectMapper.readValue(message, ProductDTO.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        List<String> productAttributes = productDTO.getProductAttribute().entrySet().stream().map(entry -> entry.getKey() + " : " + entry.getValue()).collect(Collectors.toList());
//        Product product = new Product();
//        product.setProductAttribute(productAttributes);
//        BeanUtils.copyProperties(productDTO, product);
//        //product.setProductAttribute(productAttributes);
//        solrProductRepository.save(product);
//        System.out.println("saving complete");
//    }


    @GetMapping("/ratings/{rating}")
    public Page<Product> filterRatings(@PathVariable double rating) throws IOException, SolrServerException {

//        Query query = new SimpleQuery(Criteria.where("Rating").is(rating));
//        query.setRequestHandler("browser");
//        query.addSort(Sort.by((Sort.Direction.DESC), "Weight"));
//        return solrTemplat.query("Products_ver_9", query, Product.class);

        return searchService.filterRatings(rating);

    }
    @GetMapping("/attribute/{attribute}")
    public Page<Product> filterAttributes(@PathVariable String attribute){
//        Query query = new SimpleQuery((Criteria.where("Attributes").is(attribute)));
//        query.setRequestHandler("browser");
//        return solrTemplat.query("Products_ver_9", query, Product.class);

        return searchService.filterAttributes(attribute);
    }

    @PostMapping("/search/{term}")
    public Page<Product> searchProducts(@RequestBody String term){

        return searchService.searchProducts(term);

    }

    @PostMapping("/updatePrice")
    @KafkaListener(topics = "UpdatePrice", groupId = "group_id")
    public void updatePrice(@RequestBody String message){

        ObjectMapper objectMapper = new ObjectMapper();
        PriceUpdateDTO priceUpdateDTO = new PriceUpdateDTO();
        try {
            priceUpdateDTO = objectMapper.readValue(message, PriceUpdateDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        searchService.updateProduct(priceUpdateDTO.getProductId(), priceUpdateDTO.getPrice());


    }


}
