package com.example.simo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IPAddress {

    @Id
    private String ip;

    @Enumerated(EnumType.STRING)
    private IPAddressStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
