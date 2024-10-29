package com.example.journal.FirstProject.Repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.journal.FirstProject.Entity.User;

public interface UserRepository extends MongoRepository<User,ObjectId>
{

     Optional<User> findByUsername(String username);
    
}