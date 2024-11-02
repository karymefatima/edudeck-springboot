package com.it332.edudeck.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.it332.edudeck.Entity.User;
import com.it332.edudeck.Service.UserService;

@RestController
// @CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = {"http://localhost:3000", "https://edudeck.vercel.app"})
public class RegistrationController {
    
    @Autowired
    private UserService userv;

    @PostMapping("/signup")
    public User insertUser(@RequestBody User user){
        return userv.insertUser(user);
    }
}