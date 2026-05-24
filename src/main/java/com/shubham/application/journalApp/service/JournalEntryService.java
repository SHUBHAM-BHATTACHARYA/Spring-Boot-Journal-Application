package com.shubham.application.journalApp.service;

import com.shubham.application.journalApp.entity.JournalEntry;
import com.shubham.application.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    //GET All the Requests
    public List<JournalEntry> getAllEntries(){
        return journalEntryRepository.findAll();
    }

    //GET Request by ID
    public Optional<JournalEntry> getJournalEntryById(ObjectId journalId){
        return journalEntryRepository.findById(journalId);
    }

    //POST Requests
    public JournalEntry saveEntry(JournalEntry journalEntry){
        return journalEntryRepository.save(journalEntry);
    }

    //PUT Requests
    public JournalEntry updateEntry(JournalEntry journalEntry){
        return journalEntryRepository.save(journalEntry);
    }

    //DELETE Requests
    public String deleteEntry(ObjectId journalId){
        if(journalEntryRepository.existsById(journalId)){
            journalEntryRepository.deleteById(journalId);
            return "Deleted Successfully";
        } else{
            return "Journal not found";
        }
    }
}
