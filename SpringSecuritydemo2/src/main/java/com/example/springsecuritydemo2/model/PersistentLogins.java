package com.example.springsecuritydemo2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {

    private String username;
    @Id
    private String series;
    private String token;
    private Date last_used;


}
