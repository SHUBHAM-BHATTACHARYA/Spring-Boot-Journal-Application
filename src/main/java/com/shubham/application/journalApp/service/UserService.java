package com.shubham.application.journalApp.service;

import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //GET All users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //GET user by username
    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    //POST Requests for new user
    public User saveNewUser(User user){
        return userRepository.save(user);
    }

    //POST Requests for existing user
    public User saveUser(User user){
        return userRepository.save(user);
    }

    //PUT Requests
    public User updateUser(User user){
        return userRepository.save(user);
    }

    //DELETE Requests
    public String deleteUser(String username){
        userRepository.deleteUserByUsername(username);
        return "Deleted User Successfully";
    }
}
