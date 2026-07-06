package com.shubham.application.journalApp.controller;

import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.service.UserDetailsServiceImpl;
import com.shubham.application.journalApp.service.UserService;
import com.shubham.application.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/journalApplication/public/")
public class PublicController {

    @Autowired
    private UserService userService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    //Anyone can Register's in the applications
    @PostMapping("/addUser")
    public ResponseEntity<?> createEntry(@RequestBody User user){
        try{
            if (user.getUsername() == null || user.getPassword() == null) {
                return new ResponseEntity<>("Username and Password are required", HttpStatus.BAD_REQUEST);
            }

            // Hash the password before saving for security
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            userService.saveNewUser(user);
            return new ResponseEntity<>("Successfully Registered", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if(authentication.isAuthenticated()){
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                String jwt = jwtUtil.generateToken(userDetails.getUsername());
                return new ResponseEntity<String>(jwt, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid user request.", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            return new ResponseEntity<>("Bad Username or Password", HttpStatus.BAD_REQUEST);
        }
    }

    //User can reset their password incaseof forget
    @PutMapping("/resetUserPassword/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable String username){
        try{
            Optional<User> existingUserOpt = userService.getUserByUsername(username);

            if(existingUserOpt.isPresent()){
                User existingUser = existingUserOpt.get();

                if(newUser.getPassword() == null || newUser.getPassword().isEmpty()){
                    return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
                }

                // Hash the updated password
                String encodedPassword = passwordEncoder.encode(newUser.getPassword());
                existingUser.setPassword(encodedPassword);

                userService.updateUser(existingUser);
                return new ResponseEntity<>("Password updated successfully", HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating password: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
