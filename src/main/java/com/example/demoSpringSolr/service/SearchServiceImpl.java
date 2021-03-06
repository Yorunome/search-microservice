package com.example.demoSpringSolr.service;

import com.example.demoSpringSolr.dto.ProductDTO;
import com.example.demoSpringSolr.entity.Product;
import com.example.demoSpringSolr.repository.SolrProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrProductRepository solrProductRepository;


    @Autowired
    SolrTemplate solrTemplat;

    @Autowired
    SolrClient solrClient;

    @Override
    public List<Product> searchProduct(String term) {
        String[] searchTerms = term.split(" ");
        Query query = new SimpleQuery();
//        query.setRequestHandler("browser");
        for (String terms:searchTerms) {
            query.addCriteria(Criteria.where("autoComplete").is(terms));
        }
        query.addSort(sortByWeight());
        return solrTemplat.query("Products_ver_14", query, Product.class).getContent();
    }

    @Override
    public void updateProduct(String productId, Double price) {
        PartialUpdate update = new PartialUpdate("id", productId);
        update.setValueOfField("Price", price);
        solrTemplat.saveBean("Products_ver_14",update);
        solrTemplat.commit("Products_ver_14");
    }

    @Override
    public List<Product> findProduct(String productDesc) {
        return solrProductRepository.findByProductDescription(productDesc , Sort.by(Sort.Direction.DESC , "Weight"));
    }

    @Override
    public void deleteProduct(String productid) {
        solrProductRepository.deleteByProductId(productid);
    }

    private Sort sortByWeight(){
        return Sort.by(Sort.Direction.DESC, "Weight");
    }

    private Sort sortByName(){ return Sort.by(Sort.Direction.ASC, "Name");}


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
        product.setProductAttribute(productAttributes);
        BeanUtils.copyProperties(productDTO, product);
        //product.setProductAttribute(productAttributes);
        solrProductRepository.save(product);
    }

    @Override
    public Page<Product> filterRatings(Double rating) {

        Query query = new SimpleQuery(Criteria.where("Rating").is(rating));
        query.setRequestHandler("browser");
        query.addSort(Sort.by((Sort.Direction.DESC), "Weight"));
        return solrTemplat.query("Products_ver_14", query, Product.class);

//        SolrQuery query = new SolrQuery();
//        query.addFacetField("Rating");
//
//        QueryResponse response = null;
//        try {
//            response = solrClient.query(query);
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<FacetField.Count> facetResults = response.getFacetField("category").getValues();
//        try {
//            return solrClient.query("Products_ver_11", query);
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public Page<Product> filterAttributes(@PathVariable String attribute){
        Query query = new SimpleQuery((Criteria.where("Attributes").is(attribute)));
        query.setRequestHandler("browser");
        return solrTemplat.query("Products_ver_14", query, Product.class);
    }


}
