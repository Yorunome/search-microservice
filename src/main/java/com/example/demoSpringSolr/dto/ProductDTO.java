package com.example.demoSpringSolr.dto;

import lombok.*;

import java.util.Map;

@Getter
@ToString
@Setter
public class ProductDTO {

    private String productId;

    private String productName;

    private String productDescription;

    private String productRating;

    private String productUsp;

    private String categoryId;

    private String imageUrl;

    private  Map<String, String> productAttribute;

    private Double price;

    private Double weighted;


}
