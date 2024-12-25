package com.example.ums.repositories;

import com.example.ums.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}
