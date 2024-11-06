package com.example.journal.FirstProject.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping(path="/get-journals-id/{myId}")
    public ResponseEntity<?> getById(@PathVariable ObjectId myId)
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        List<JournalEntry> entries=journalEntryService.getByUsername(username);
        List<JournalEntry> collect=entries.stream().filter(x->x.getId().equals(myId)).collect(Collectors.toList());
        
        if(!collect.isEmpty())
        {
            Optional<JournalEntry> journalEntry=journalEntryService.findById(myId);
            if(journalEntry.isPresent())
            {
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
        }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @GetMapping(path="/get-journals")
    public ResponseEntity<?> getAll()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
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



    @PostMapping(path="/create")
    public ResponseEntity<JournalEntry> creatEntry(@RequestBody JournalEntry myEntry)
    {
        try
        {
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
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
    

    @PutMapping("/update/{id}")
    public ResponseEntity<JournalEntry> updateJournal(@PathVariable ObjectId id,
                                                    @RequestBody JournalEntry newEntry)
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();

        List<JournalEntry> entries=journalEntryService.getByUsername(username);
        List<JournalEntry> collect=entries.stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());

        
        if(!collect.isEmpty())
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

        }
        
       
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }


    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id)
    {

        try {
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
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