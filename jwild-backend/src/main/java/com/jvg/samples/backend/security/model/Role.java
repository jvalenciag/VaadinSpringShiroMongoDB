package com.jvg.samples.backend.security.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by COVICEL on 28/01/2015.
 */
@Document
public class Role {

    public static String COLLECTION_NAME = "role";

    @Id
    private ObjectId id;

    @NotNull
    private String name;

    private String description;

    private Set<String> permissions;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
