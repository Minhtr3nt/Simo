package com.example.simo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    private String userName;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
            ,inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name")
    )
    private List<Role> roles;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "key_id")
    private Key key;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> token;

}
