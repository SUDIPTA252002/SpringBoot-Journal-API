package com.example.journal.FirstProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Service.UserService;

@RestController
@RequestMapping(path = "/public")
public class PublicController 
{
    @Autowired
    private UserService userService;

    @PostMapping(value="/create")
    public ResponseEntity<?> creatEntry(@RequestBody User user)
    {
        try
        {   
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }         
    }
    
}
