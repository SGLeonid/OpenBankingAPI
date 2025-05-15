package com.openbanking.openbanking.api.response;

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
