package com.openbanking.openbanking.account.impl.response;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BalanceInfo {
    private Float usdBalance;
    private Float eurBalance;
}
