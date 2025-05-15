package com.openbanking.openbanking.account.impl.response;

import com.openbanking.openbanking.account.impl.entities.Transaction;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionsResponse {
    private ResponseBase responseBase;
    private List<Transaction> transactions;
}
