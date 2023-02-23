package com.example.multiplehttpsecurity.util;

import com.example.multiplehttpsecurity.model.Provider;
import com.example.multiplehttpsecurity.model.Users;
import com.example.multiplehttpsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public void processOAuthPostLogin(String username) {
        Users existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            Users newUser = new Users();
            newUser.setUsername(username);
            newUser.setProvider(Provider.GOOGLE);

            repo.save(newUser);
        }

    }
}
