package com.jvg.samples.backend.security.dao;

import com.jvg.samples.backend.security.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by COVICEL on 28/01/2015.
 */
@Component("userDAO")
public class UserDAOimpl implements UserDAO {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findOne(ObjectId userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(ObjectId userId) {
        userRepository.delete(userId);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
