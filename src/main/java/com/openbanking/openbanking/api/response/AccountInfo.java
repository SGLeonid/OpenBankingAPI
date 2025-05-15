package com.openbanking.openbanking.api.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountInfo {
    private String iban;
    private String surname;
    private String firstName;
    private Float balance;
}
