package com.openbanking.openbanking.api.response;

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
