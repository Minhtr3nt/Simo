package com.example.simo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "`key`")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    private String consumerKey;
    private String secretKey;

    @OneToOne(mappedBy = "key")
    private User user;

}
