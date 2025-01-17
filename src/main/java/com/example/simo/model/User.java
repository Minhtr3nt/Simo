package com.example.simo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    @Column(unique = true)
    private String userName;
    private String password;
    @Column(unique = true)
    private String consumerKey;
    private String secretKey;

    @ManyToMany
    @JoinTable(
            name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
            ,inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name")
    )
    private Set<Role> roles;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IPAddress> IPsAddress;

}
