package com.example.journal.FirstProject.Controller;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController 
{
    @Autowired
    private UserService userService;

    @GetMapping(path="/id/{id}")
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

    @GetMapping(path="/username/{username}")
    public ResponseEntity<User> getById(@PathVariable String username)
    {
        Optional<User> user=userService.findByUsername(username);
        if(user.isPresent())
        {
            return new ResponseEntity<>(user.get(),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size)
    {
        Pageable pageable=PageRequest.of(page, size);
        Page<User> all=userService.getAll(pageable);
        if(all.hasContent())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }





     @PostMapping(path="/create")
    public ResponseEntity<?> creatEntry(@RequestBody User user)
    {
        try
        {   
            userService.saveUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }         
    }
    

    @PutMapping("/{username}")
    public ResponseEntity<User> updateJournal(@PathVariable String username,@RequestBody User newUser)
    {
        Optional<User> user=userService.findByUsername(username);
        User old=user.get();
        if(user.isPresent())
        {
            old.setUsername(newUser.getUsername()!=null&&!newUser.getUsername().equals("")?
                                        newUser.getUsername():old.getUsername());

            old.setPassword(newUser.getPassword()!=null&&!newUser.getPassword().equals("")?
                                        newUser.getPassword():old.getPassword());

            userService.saveUser(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




      @DeleteMapping(path="/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id)
    {

        Optional<User> user=userService.getById(id);
        if(user.isPresent())
        {
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
