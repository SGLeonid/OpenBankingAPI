package com.openbanking.openbanking.api.request;

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
