package com.jvg.samples.backend.data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@Document(collection = Category.COLLECTION_NAME)
public class Category implements Serializable {

    public static final String COLLECTION_NAME = "category";

    @Id
    private ObjectId id;

    @NotNull
    private String name;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
