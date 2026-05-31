package com.shubham.application.journalApp.controller;

import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journalApplication/public/")
public class PublicController {

    @Autowired
    private UserService userService;

    //Anyone can Register's in the applications
    @PostMapping("/addUser")
    public ResponseEntity<?> createEntry(@RequestBody User user){
        try{
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //User can reset their password incaseof forget
    @PutMapping("/resetUserPassword/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable String username){
        try{
            User existingUser = userService.getUserByUsername(username).get();
            if(existingUser!=null){
                //username is unique
                existingUser.setUsername(existingUser.getUsername());
                //user can reset their password
                existingUser.setPassword(newUser.getPassword());
                //only admin can update the roles
                existingUser.setRoles(existingUser.getRoles());
                userService.updateUser(existingUser);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
