package com.example.journal.FirstProject.Service;


import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.journal.FirstProject.Repository.JournalEntryRepository;
import com.example.journal.FirstProject.Entity.JournalEntry;


@Component
public class JournalEntryService 
{
    @Autowired
    private JournalEntryRepository journalEntryRepo;
    

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

    public void deleteId(ObjectId id)
    {

        journalEntryRepo.deleteById(id);        
    }
}
