package com.example.journal.FirstProject.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.journal.FirstProject.Entity.JournalEntry;
import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Service.JournalEntryService;
import com.example.journal.FirstProject.Service.UserService;


@RestController
@RequestMapping(value="/admin")
public class AdminController 
{

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping(path="/allJournals")
    public ResponseEntity<?> getAllJournals(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable =PageRequest.of(page, size);

        Page<JournalEntry> all=journalEntryService.getAll(pageable);
        if(all.hasContent())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    


    
    @GetMapping(path="/allUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size)
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


    @PostMapping(path="/create-user-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user)
    {
        try
        {
            userService.saveUserAdmin(user);
            Optional<User> u=userService.getById(user.getUserID());
            if(u.isPresent())
            {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
