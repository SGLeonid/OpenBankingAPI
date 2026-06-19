package org.forestwizard.springdemo.wallet;

import org.forestwizard.springdemo.authentication.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepositoryI extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByIban(String iban);
    Optional<Wallet> findByUserAndIban(AuthenticatedUser user, String iban);
}
