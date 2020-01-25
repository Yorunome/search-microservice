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

    private Double productRating;

    private String productUsp;

    private String categoryName;

    private String imageUrl;

    private  Map<String, String> productAttribute;

    private Double price;

    private Double weighted;


}
