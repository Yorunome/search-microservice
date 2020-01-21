package com.example.demoSpringSolr.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demoSpringSolr.dto.ProductDTO;
import com.example.demoSpringSolr.entity.Product;
import com.example.demoSpringSolr.repository.SolrOrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Products")
public class OrderController {

    @Autowired
    SolrOrderRepository solrOrderRepository;

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

    @PostMapping("/addProduct")
    @KafkaListener(topics = "Products", groupId = "group_id")
    public void consume(@RequestBody String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = new ProductDTO();
        try {
            productDTO = objectMapper.readValue(message, ProductDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<String> productAttributes = productDTO.getProductAttribute().entrySet().stream().map(entry -> entry.getKey() + "#" + entry.getValue()).collect(Collectors.toList());
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setProductAttribute(productAttributes);
        solrOrderRepository.save(product);
    }

}
