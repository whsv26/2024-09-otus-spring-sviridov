package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User implements UserDetails {

    @Id
    private String id;

    private Set<GrantedAuthority> authorities;

    private String username;

    private String password;
}
