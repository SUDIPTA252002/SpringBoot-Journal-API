package com.example.journal.FirstProject.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.journal.FirstProject.Repository.JournalEntryRepository;

import com.example.journal.FirstProject.Entity.JournalEntry;
import com.example.journal.FirstProject.Entity.User;


@Component
public class JournalEntryService 
{
    @Autowired
    private JournalEntryRepository journalEntryRepo;
    @Autowired
    private UserService userService;
    

    public void saveEntry(JournalEntry journalEntry)
    {
        journalEntryRepo.save(journalEntry);
    } 
    public Optional<JournalEntry> findById(ObjectId Id)
    {
        return journalEntryRepo.findById(Id);
    }

    public Page<JournalEntry> getAll(Pageable pageable)
    {
        return journalEntryRepo.findAll(pageable);
    }

    public List<JournalEntry> All()
    {
        return journalEntryRepo.findAll();
    }

    public void deleteId(ObjectId id, String username)
    {
        Optional<User> u=userService.findByUsername(username);
        User user=u.get();
        user.getJournalEntries().removeIf(x->x.getId().equals(id));
        userService.saveUser(user);
        journalEntryRepo.deleteById(id);        
    }
    public void saveEntry(JournalEntry myEntry, String username) 
    {
       
       Optional<User> u=userService.findByUsername(username);
       if(u.isPresent())
       {
            User user=u.get();
            myEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepo.save(myEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);

       }
       else
       {
            throw new RuntimeException("Username not found");
       }
       
    }
    
}