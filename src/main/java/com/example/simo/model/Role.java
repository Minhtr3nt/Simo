package com.example.simo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Role {

    @Id
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
