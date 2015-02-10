package com.jvg.samples.backend.config;

import com.jvg.samples.backend.DataService;
import com.jvg.samples.backend.mock.MockDataGenerator;
import com.jvg.samples.backend.mock.MockDataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jose on 27/01/15.
 */
@Configuration
@ComponentScan(basePackages = "com.jvg.samples.backend")
public class DataServiceTestConfig {

    @Bean
    public DataService dataService() {
        DataService dataService = new MockDataService();
        // set properties, etc.
        return dataService;
    }

}
