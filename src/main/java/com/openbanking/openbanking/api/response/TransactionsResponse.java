package com.openbanking.openbanking.api.response;

import com.openbanking.openbanking.api.entities.Transaction;
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
