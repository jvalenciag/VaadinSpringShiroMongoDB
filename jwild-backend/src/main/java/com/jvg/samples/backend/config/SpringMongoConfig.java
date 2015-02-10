package com.jvg.samples.backend.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

/**
 * Created by jose on 27/01/15.
 */
@Configuration
@ComponentScan(basePackages = {"com.jvg.samples.backend"})
@EnableMongoRepositories(basePackages = {"com.jvg.samples.backend"})
public class SpringMongoConfig extends AbstractMongoConfiguration {

    @Override
    public String getDatabaseName() {
        String db_name = System.getenv("OPENSHIFT_APP_NAME");
        return db_name==null?"sampledb":db_name;
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        //if(host==null) return new MongoClient("192.168.0.4");
        if(host==null) return new MongoClient("127.0.0.1");
        int port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
        String mongodb_username=System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String mongodb_password=System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        MongoCredential credential = MongoCredential.createMongoCRCredential(mongodb_username, getDatabaseName(), mongodb_password.toCharArray());
        return new MongoClient(new ServerAddress(host,port) , Arrays.asList(credential));
    }

}
