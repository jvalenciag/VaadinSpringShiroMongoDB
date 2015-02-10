package com.jvg.samples.backend;

import java.util.Collection;

import com.jvg.samples.backend.data.Category;
import com.jvg.samples.backend.data.Product;
import com.jvg.samples.backend.mock.MockDataService;
import com.vaadin.data.Container;
import org.bson.types.ObjectId;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class DataService {

    public abstract Collection<Product> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);

    public abstract void deleteProduct(Object productId);

    public abstract Product getProductById(Object productId);

    public abstract Container getCategoriesContainer();

    public abstract Container getProductsContainer();

    /*public static DataService get() {
        return MockDataService.getInstance();
    }*/

}
