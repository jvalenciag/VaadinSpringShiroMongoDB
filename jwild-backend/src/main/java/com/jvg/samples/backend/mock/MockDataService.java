package com.jvg.samples.backend.mock;

import com.jvg.samples.backend.DataService;
import com.jvg.samples.backend.data.Category;
import com.jvg.samples.backend.data.Product;
import com.vaadin.data.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.tylproject.vaadin.addon.MongoContainer;

import java.util.List;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class MockDataService extends DataService {

    @Autowired
    public MongoTemplate mongoTemplate;

     public MockDataService() {

    }

    @Override
    public synchronized List<Product> getAllProducts() {
        return mongoTemplate.findAll(Product.class);
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return mongoTemplate.findAll(Category.class);
    }

    @Override
    public Container getCategoriesContainer() {
        return MongoContainer.Builder.forEntity(Category.class,mongoTemplate).build();
    }

    @Override
    public Container getProductsContainer() {
        return MongoContainer.Builder.forEntity(Product.class, mongoTemplate).build();
        //.withFilterConverter(new MongoFilterConverter())
    }

    @Override
    public synchronized void updateProduct(Product p) {
        mongoTemplate.save(p);
    }

    @Override
    public synchronized Product getProductById(Object productId) {
        return mongoTemplate.findById(productId,Product.class);
    }

    @Override
    public synchronized void deleteProduct(Object productId) {

        mongoTemplate.remove( new Query(Criteria.where("id").is(productId)),Product.class);
    }
}
