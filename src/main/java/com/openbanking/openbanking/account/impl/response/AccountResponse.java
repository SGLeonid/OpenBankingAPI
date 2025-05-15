package com.openbanking.openbanking.account.impl.response;

import com.openbanking.openbanking.account.impl.entities.Account;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountResponse {
    private ResponseBase responseBase;
    private AccountInfo account;
}
