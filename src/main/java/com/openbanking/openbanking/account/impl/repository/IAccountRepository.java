package com.openbanking.openbanking.account.impl.repository;

import com.openbanking.openbanking.account.impl.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    Account findByIban(String iban);
}
