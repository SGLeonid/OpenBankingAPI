package com.openbanking.openbanking.api.repository;

import com.openbanking.openbanking.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    Account findByIban(String iban);
}
