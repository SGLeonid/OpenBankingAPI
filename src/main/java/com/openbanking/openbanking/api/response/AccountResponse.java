package com.openbanking.openbanking.api.response;

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
