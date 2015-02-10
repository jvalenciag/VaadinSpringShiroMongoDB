package com.jvg.samples.backend;

import com.jvg.samples.backend.config.DataServiceTestConfig;
import com.jvg.samples.backend.config.SpringMongoConfig;
import org.junit.Before;
import org.junit.Test;
import com.jvg.samples.backend.data.Product;
import com.jvg.samples.backend.mock.MockDataService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Simple unit test for the back-end data service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class,
        classes = {SpringMongoConfig.class, DataServiceTestConfig.class})
public class DataServiceTest {

    @Autowired
    private DataService service;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testDataServiceCanFetchProducts() throws Exception {
        assertFalse(service.getAllProducts().isEmpty());
    }

    @Test
    public void testDataServiceCanFetchCategories() throws Exception {
        assertFalse(service.getAllCategories().isEmpty());
    }

    @Test
    public void testUpdateProduct_updatesTheProduct() throws Exception {
        Product p = service.getAllProducts().iterator().next();
        p.setProductName("My Test Name");
        service.updateProduct(p);
        Product p2 = service.getAllProducts().iterator().next();
        assertEquals("My Test Name", p2.getProductName());
    }
}
