package com.openbanking.openbanking.api.service;

import com.openbanking.openbanking.api.entities.Account;
import com.openbanking.openbanking.api.entities.Transaction;

import java.util.List;

public interface OpenBankingService {
    List<Account> findAllAccounts();
    void createAccount(Account account);
    Account getAccountById(Long id);
    Account getAccountByIban(String iban);
    boolean deleteAccountById(Long id);
    boolean updateAccount(Long Id, Account account);

    List<Transaction> findAllTransactions();
    void createTransaction(Transaction transaction);
    Transaction getTransactionById(Long id);
    boolean deleteTransactionById(Long id);
    boolean updateTransaction(Long id, Transaction transaction);
    List<Transaction> findTransactionsBySenderAccountId(Long accountId);
}
