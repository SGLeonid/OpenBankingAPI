package org.forestwizard.springdemo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private UUID paymentId;
    private String senderId;
    private String receiverId;
    private String currency;
    private BigDecimal amount;
    private String status;
}
