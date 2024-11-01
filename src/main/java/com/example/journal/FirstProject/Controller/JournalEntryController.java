package com.example.journal.FirstProject.Controller;

import java.util.List;
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

import com.example.journal.FirstProject.Entity.JournalEntry;
import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Service.JournalEntryService;
import com.example.journal.FirstProject.Service.UserService;



@RestController
@RequestMapping(value="/journal")
public class JournalEntryController 
{
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping(path="/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId)
    {
        Optional<JournalEntry> journalEntry=journalEntryService.findById(myId);
        if(journalEntry.isPresent())
        {
            return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/{username}")
    public ResponseEntity<?> getAll(@PathVariable String username)
    {
        Optional<User> user=userService.findByUsername(username);
        if(user.isPresent())
        {
            User u=user.get();
            List<JournalEntry> all=u.getJournalEntries();
            if(!all.isEmpty())
            {
                return new ResponseEntity<>(all,HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @GetMapping(path="/all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size)
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

    @PostMapping(path="/create/{username}")
    public ResponseEntity<JournalEntry> creatEntry(@RequestBody JournalEntry myEntry,@PathVariable String username)
    {
        try
        {

            journalEntryService.saveEntry(myEntry,username);
            return new ResponseEntity<>(HttpStatus.CREATED);

        }
        catch(RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }         
    }
    

    @PutMapping("/id/{username}/{id}")
    public ResponseEntity<JournalEntry> updateJournal(@PathVariable ObjectId id,
                                                    @RequestBody JournalEntry newEntry,
                                                    @PathVariable String username)
    {
        Optional<JournalEntry> journalEntry=journalEntryService.findById(id);
        JournalEntry old=journalEntry.get();
        if(journalEntry.isPresent())
        {
            journalEntry.get().setTitle(newEntry.getTitle()!=null&&!newEntry.getTitle().equals("")?
                                        newEntry.getTitle():old.getTitle());

            journalEntry.get().setContent(newEntry.getContent()!=null&&!newEntry.getContent().equals("")?
                                        newEntry.getContent():old.getContent());

            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping(path="/id/{username}/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id,@PathVariable String username)
    {

        try {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            
            if (journalEntry.isPresent()) {
                journalEntryService.deleteId(id, username);
                
                Optional<JournalEntry> deletedEntryCheck = journalEntryService.findById(id);
                if (!deletedEntryCheck.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 
        catch (Exception e) 
        { 
            e.printStackTrace(); 
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
} 