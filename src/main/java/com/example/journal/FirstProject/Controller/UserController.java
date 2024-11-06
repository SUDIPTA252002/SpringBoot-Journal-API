package com.example.journal.FirstProject.Controller;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController 
{
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping(path="/fetch/{id}")
    public ResponseEntity<User> getById(@PathVariable ObjectId id)
    {
        Optional<User> user=userService.getById(id);
        if(user.isPresent())
        {
            return new ResponseEntity<>(user.get(),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(path="/fetch")
    public ResponseEntity<User> getById()
    {
        Authentication authenctication=SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user=userService.findByUsername(authenctication.getName());
        if(user.isPresent())
        {
            return new ResponseEntity<>(user.get(),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    

    @PutMapping("/update")
    public ResponseEntity<User> updateJournal(@RequestBody User newUser)
    {
        Authentication autehnctication=SecurityContextHolder.getContext().getAuthentication();
        String username=autehnctication.getName();
        Optional<User> user=userService.findByUsername(username);
        User old=user.get();
        if(user.isPresent())
        {
            old.setUsername(newUser.getUsername()!=null&&!newUser.getUsername().equals("")?
                                        newUser.getUsername():old.getUsername());

            old.setPassword(newUser.getPassword()!=null&&!newUser.getPassword().equals("")?
                                        passwordEncoder.encode(newUser.getPassword()):old.getPassword());

            userService.saveUser(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping(path="/delete")
    public ResponseEntity<?> deleteById()
    {
        Authentication authenctication=SecurityContextHolder.getContext().getAuthentication();
        Optional<User> u=userService.findByUsername(authenctication.getName());
        Optional<User> user=userService.getById(u.get().getUserID());
        if(user.isPresent())
        {
            ObjectId id=user.get().getUserID();
            userService.deleteById(id);
            Optional<User> deletedEntryCheck = userService.getById(id);
            if (!deletedEntryCheck.isPresent()) 
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } 
            else
            {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
