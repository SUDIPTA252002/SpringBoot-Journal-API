package com.example.journal.FirstProject.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.journal.FirstProject.Service.CustomUserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;



@Configuration
@EnableWebSecurity
public class SpringSecurity
{

     private CustomUserDetailsServiceImpl userDetailsService;

    SpringSecurity(CustomUserDetailsServiceImpl userDetailsService) 
    {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception
    {
        http
            .authorizeHttpRequests((auths)->auths
                                            .requestMatchers("/journal/**","/user/**").authenticated()
                                            .requestMatchers("/admin/**").hasRole("ADMIN")
                                            .requestMatchers("/public/**").permitAll()
                                            .anyRequest().permitAll()
            )
            .httpBasic(withDefaults())
           .csrf(csrf->csrf.disable())
           .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder auth=http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth.build();
       
    }
    
}
