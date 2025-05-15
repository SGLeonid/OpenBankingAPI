package com.openbanking.openbanking.api.service;

import com.openbanking.openbanking.api.entities.Account;
import com.openbanking.openbanking.api.repository.IAccountRepository;
import com.openbanking.openbanking.api.repository.ITransactionRepository;
import com.openbanking.openbanking.api.entities.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpenBankingServiceImpl implements OpenBankingService {
    // Store both repositories in a single service. A constructor is used
    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;

    public OpenBankingServiceImpl(IAccountRepository accountRepository, ITransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

    @Override
    public boolean updateAccount(Long id, Account updatedAccount) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        Account account = accountOptional.get();
        if (account.getId().equals(id)) {
            account.setIban(updatedAccount.getIban());
            account.setSurname(updatedAccount.getSurname());
            account.setFirstname(updatedAccount.getFirstname());
            account.setUsdBalance(updatedAccount.getUsdBalance());
            account.setEurBalance(updatedAccount.getEurBalance());
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAccountById(Long id) {
        try {
            accountRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteTransactionById(Long id) {
        try {
            transactionRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateTransaction(Long id, Transaction updatedTransaction) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        Transaction transaction = transactionOptional.get();
        if (transaction.getId().equals(id)) {
            transaction.setSenderAccountId(updatedTransaction.getSenderAccountId());
            transaction.setReceiverAccountId(updatedTransaction.getReceiverAccountId());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setCurrency(updatedTransaction.getCurrency());
            return true;
        }
        return false;
    }

    @Override
    public List<Transaction> findTransactionsBySenderAccountId(Long accountId) {
        return transactionRepository.findBySenderAccountId(accountId);
    }
}
