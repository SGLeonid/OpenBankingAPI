package com.openbanking.openbanking.api.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    private String iban;
}
