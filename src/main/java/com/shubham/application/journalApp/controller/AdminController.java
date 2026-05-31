package com.shubham.application.journalApp.controller;

import com.shubham.application.journalApp.entity.JournalEntry;
import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.service.JournalEntryService;
import com.shubham.application.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journalApplication/admin/")
public class AdminController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    //Only ADMIN can see all the USER Registered
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUser(){
        try{
            List<User> allUsers = userService.getAllUsers();
            if(allUsers!=null && !allUsers.isEmpty()){
                return new ResponseEntity<>(allUsers, HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Only ADMIN can see all the Journal USER Registered
    @GetMapping("/getAllJournal")
    public ResponseEntity<?> getAllJournal(){
        try{
            List<JournalEntry> allJournal = journalEntryService.getAllEntries();
            if(allJournal!=null && !allJournal.isEmpty()){
                return new ResponseEntity<>(allJournal, HttpStatus.OK);
            } else{
                return new ResponseEntity<>("No Journal exists", HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Only ADMIN can see particular USER by providing username
    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String username){
        try{
            Optional<User> user = userService.getUserByUsername(username);
            if(user.isPresent()){
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(String.format("%s user not present", username), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Only ADMIN can update any user's roles
    @PutMapping("/updateUserRoles/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable String username){
        try{
            User existingUser = userService.getUserByUsername(username).get();
            if(existingUser!=null){
                //username is unique
                existingUser.setUsername(existingUser.getUsername());
                //user can update their password
                existingUser.setPassword(existingUser.getPassword());
                //only admin can update the roles
                existingUser.setRoles(newUser.getRoles());
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
