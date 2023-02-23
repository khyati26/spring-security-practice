package com.example.springsecuritydemo2.util;

import com.example.springsecuritydemo2.model.Users;
import com.example.springsecuritydemo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("User not found with username: " + username);
        else
//            return new MyUserDetails(username.toLowerCase(),username.toLowerCase(),username.toUpperCase());
            return new MyUserDetails(user.getUsername(), user.getPassword(), user.getMyrole());
    }
}
