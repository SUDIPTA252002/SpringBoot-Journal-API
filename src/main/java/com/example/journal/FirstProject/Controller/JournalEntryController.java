package com.example.journal.FirstProject.Controller;

import java.time.LocalDateTime;

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
import com.example.journal.FirstProject.Service.JournalEntryService;



@RestController
@RequestMapping(value="/journal")
public class JournalEntryController 
{
    @Autowired
    private JournalEntryService journalEntryService;

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

    @GetMapping(path="/all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "10") int page,@RequestParam(defaultValue = "10") int size)
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

    @PostMapping(path="/create")
    public ResponseEntity<JournalEntry> creatEntry(@RequestBody JournalEntry myEntry)
    {
        try
        {
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry);
            return new ResponseEntity<>(HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }         
    }
    

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournal(@PathVariable ObjectId id,@RequestBody JournalEntry newEntry)
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


    @DeleteMapping(path="/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id)
    {

        Optional<JournalEntry> journalEntry=journalEntryService.findById(id);
        if(journalEntry.isPresent())
        {
            journalEntryService.deleteId(id);
            Optional<JournalEntry> deletedEntryCheck = journalEntryService.findById(id);
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