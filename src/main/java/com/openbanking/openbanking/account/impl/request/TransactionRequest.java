package com.openbanking.openbanking.account.impl.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    String senderIban;
    String receiverIban;
    Float amount;
    String currency;
}
