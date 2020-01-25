package com.example.demoSpringSolr.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class PriceUpdateDTO {

    private String productId;
    private Double price;
}
