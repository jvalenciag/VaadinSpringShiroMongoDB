package com.jvg.samples.backend.security.dao;

import com.jvg.samples.backend.security.model.User;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by COVICEL on 28/01/2015.
 */
public interface UserDAO {
    User findOne(ObjectId userId);

    User findByUsername(String username);

    void createUser(User user);

    List<User> getAllUsers();

    void deleteUser(ObjectId userId);

    void updateUser(User user);
}
