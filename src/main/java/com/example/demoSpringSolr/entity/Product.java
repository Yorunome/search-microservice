package com.example.demoSpringSolr.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import org.springframework.data.solr.core.mapping.Indexed;

import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;
import java.util.List;

@SolrDocument(solrCoreName = "Products_ver_5")
@Getter
@ToString
@Setter
public class Product implements Serializable{

    @Id

    @Indexed(name = "pid", type = "string")

    private String productId;

    @Indexed(name = "pname", type = "string")

    private String productName;

    @Indexed(name = "pdesc", type = "string")

    private String productDescription;

    @Indexed(name = "pattr", type = "string")

    private List <String> productAttribute;

    @Indexed(name = "prat", type = "double")

    private String productRating;

    @Indexed(name = "pusp", type = "string")

    private String productUsp;

    @Indexed(name = "cid", type = "string")
    private String categoryId;

    @Indexed(name = "url", type = "string")
    private String imageUrl;

//    @Indexed(name = "color", type = "string")
//    private String color;
//
//    @Indexed(name = "Connectivity", type = "string")
//    private String Connectivity;


}
