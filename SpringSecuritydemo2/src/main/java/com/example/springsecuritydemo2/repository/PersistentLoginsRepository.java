package com.example.springsecuritydemo2.repository;

import com.example.springsecuritydemo2.model.PersistentLogins;
import com.example.springsecuritydemo2.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, String> {

    void deleteByUsername(String username);
}
