package com.example.journal.FirstProject.Service;

import java.util.Arrays;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Repository.UserRepository;

@Component
public class UserService 
{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getById(ObjectId id)
    {
        return userRepo.findById(id);
    }
    
    public Page<User> getAll(Pageable pageable)
    {
        return userRepo.findAll(pageable);
    }

    public void saveUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepo.save(user);
    }

    public void deleteById(ObjectId id)
    {
        userRepo.deleteById(id);
    }

    public Optional<User> findByUsername(String username)
    {
        return userRepo.findByUsername(username);
    }
}
