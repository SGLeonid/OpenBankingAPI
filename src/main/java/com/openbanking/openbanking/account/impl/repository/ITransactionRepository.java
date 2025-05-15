package com.openbanking.openbanking.account.impl.repository;

import com.openbanking.openbanking.account.impl.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountId(Long id);
}
