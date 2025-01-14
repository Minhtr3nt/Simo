package com.example.simo.model;

import jakarta.persistence.*;
import lombok.Data;
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
}
