package com.openbanking.openbanking.account.impl.response;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
    private ResponseBase responseBase;
    private BalanceInfo balanceInfo;
}
