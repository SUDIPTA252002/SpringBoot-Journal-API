package com.example.journal.FirstProject.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.journal.FirstProject.Entity.User;
import com.example.journal.FirstProject.Repository.UserRepository;

@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService 
{
    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException
    {
        Optional<User> u=userRepo.findByUsername(username);
        if(u.isPresent())
        {
            User user=u.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();

        }
        throw new UsernameNotFoundException("Username not found with username:"+username);
    }

    
}
