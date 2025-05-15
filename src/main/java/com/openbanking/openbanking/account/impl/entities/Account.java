package com.openbanking.openbanking.account.impl.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "iban")
    private String iban;

    @Column(name = "surname")
    private String surname;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "usd_balance")
    private Float usdBalance;

    @Column(name = "eur_balance")
    private Float eurBalance;
}
