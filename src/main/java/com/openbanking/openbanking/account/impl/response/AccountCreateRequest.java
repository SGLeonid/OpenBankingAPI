package com.openbanking.openbanking.account.impl.response;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {
    private String surname;
    private String firstname;
}
