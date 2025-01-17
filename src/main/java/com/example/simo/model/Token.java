package com.example.simo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    //private boolean available;

    @Column(length = 500)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
