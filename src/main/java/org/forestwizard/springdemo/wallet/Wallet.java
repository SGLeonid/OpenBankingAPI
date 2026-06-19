package org.forestwizard.springdemo.wallet;

import jakarta.persistence.*;
import lombok.*;
import org.forestwizard.springdemo.authentication.AuthenticatedUser;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AuthenticatedUser user;
    private String iban;
    private BigDecimal usdAmount;
    private BigDecimal eurAmount;
    private BigDecimal uahAmount;
}
