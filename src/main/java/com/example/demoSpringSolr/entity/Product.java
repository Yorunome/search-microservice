package com.example.demoSpringSolr.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import org.springframework.data.solr.core.mapping.Indexed;

import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;
import java.util.List;

@SolrDocument(solrCoreName = "Products_ver_8")
@Getter
@ToString
@Setter
public class Product implements Serializable{

    @Id

    @Indexed(name = "Product ID", type = "string")

    private String productId;

    @Indexed(name = "Name", type = "string")

    private String productName;

    @Indexed(name = "Description", type = "string")

    private String productDescription;

    @Indexed(name = "Attributes", type = "string")

    private List <String> productAttribute;

    @Indexed(name = "Rating", type = "double")

    private String productRating;

    @Indexed(name = "USP", type = "string")

    private String productUsp;

    @Indexed(name = "Category ID", type = "string")
    private String categoryId;

    @Indexed(name = "url", type = "string")
    private String imageUrl;

    @Indexed(name = "Price", type = "double")
    private Double price;

    @Indexed(name = "Weight", type = "double")
    private Double weighted;


}
