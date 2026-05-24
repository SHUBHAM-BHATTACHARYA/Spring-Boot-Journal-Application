package com.shubham.application.journalApp.repository;

import com.shubham.application.journalApp.entity.JournalEntry;
import com.shubham.application.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    // Spring automatically generates: {"username" : username}
    Optional<User> findByUsername(String username);

    void deleteUserByUsername(String username);
}
