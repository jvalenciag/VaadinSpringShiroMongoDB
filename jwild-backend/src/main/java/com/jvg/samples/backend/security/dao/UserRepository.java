package com.jvg.samples.backend.security.dao;

import com.jvg.samples.backend.security.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by COVICEL on 28/01/2015.
 */
@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}
