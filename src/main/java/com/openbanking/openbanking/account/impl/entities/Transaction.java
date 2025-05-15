package com.openbanking.openbanking.account.impl.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_account_id")
    private Long senderAccountId;

    @Column(name = "receiver_account_id")
    private Long receiverAccountId;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "currency")
    private String currency;
}
