package org.forestwizard.springdemo.transaction;

import org.forestwizard.springdemo.authentication.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryI extends JpaRepository<Transaction, UUID> {
    public List<Transaction> findBySender(AuthenticatedUser sender);
    public List<Transaction> findBySenderIbanAndReceiverIban(String senderIban, String receiverIban);
    List<Transaction> findTop10BySenderOrderByIdDesc(AuthenticatedUser sender);
}
