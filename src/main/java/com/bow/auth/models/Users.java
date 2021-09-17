package com.bow.auth.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Users")
public class Users {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<Role>();
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
