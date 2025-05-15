package com.openbanking.openbanking.account.impl.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseBase {
    private Integer status;
    private String message;
}
