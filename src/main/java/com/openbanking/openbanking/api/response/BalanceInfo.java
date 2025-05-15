package com.openbanking.openbanking.api.response;

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
