package com.openbanking.openbanking.account.impl;

import com.openbanking.openbanking.account.impl.entities.Account;
import com.openbanking.openbanking.account.impl.request.TransactionRequest;
import com.openbanking.openbanking.account.impl.response.AccountCreateRequest;
import com.openbanking.openbanking.account.impl.response.BalanceInfo;
import com.openbanking.openbanking.account.impl.service.OpenBankingService;
import com.openbanking.openbanking.account.impl.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenBankingController {
    private final OpenBankingService openBankingService;

    // Get the balance of the current account
    @GetMapping(path = "/accounts/{accountId}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BalanceInfo> getBalance(@PathVariable("accountId") String accountId) {
        // Get the account with the specified IBAN and check if it exists
        Account account = openBankingService.getAccountByIban(accountId);
        if (account == null) {
            return new ResponseEntity<>(new BalanceInfo(0f, 0f), HttpStatus.NOT_FOUND);
        }

        // Get the balance info and return it
        BalanceInfo balanceInfo = new BalanceInfo(account.getUsdBalance(), account.getEurBalance());
        return new ResponseEntity<>(balanceInfo, HttpStatus.OK);
    }

    // Get last 10 transactions info of the current account
    @GetMapping(path = "/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<Transaction>> getTransactions(@PathVariable("accountId") String accountId) {
        // Get the account with the specified IBAN and check if it exists
        Account account = openBankingService.getAccountByIban(accountId);
        if (account == null) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        // Get the list of transactions where the sender is the specified account
        // If there are more than 10 transactions, return the latest 10 instances
        List<Transaction> transactions = openBankingService.findTransactionsBySenderAccountId(account.getId());
        if (transactions.size() > 10) {
            int size = transactions.size();
            transactions = transactions.subList(size - 10, size);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Initiate payment process
    @PostMapping(path = "/payments/initiate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> initiateTransaction(@RequestBody TransactionRequest transactionRequest) {
        // Get info about both accounts and check if they exist
        Account senderAccount = openBankingService.getAccountByIban(transactionRequest.getSenderIban());
        Account receiverAccount = openBankingService.getAccountByIban(transactionRequest.getReceiverIban());
        if (senderAccount == null || receiverAccount == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        // Make sure the sender has enough money to make the transaction
        if (senderAccount.getUsdBalance() < transactionRequest.getAmount()) {
            return new ResponseEntity<>("Not enough money", HttpStatus.BAD_REQUEST);
        }

        // Create a new transaction instance
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(senderAccount.getId());
        transaction.setReceiverAccountId(receiverAccount.getId());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setCurrency(transactionRequest.getCurrency());

        // Change balances according to the amount of money in the transaction
        // Not the best solution of multiple choice but for the first time is OK :D
        if (transactionRequest.getCurrency().equals("USD")) {
            senderAccount.setUsdBalance(senderAccount.getUsdBalance() - transaction.getAmount());
            receiverAccount.setUsdBalance(receiverAccount.getUsdBalance() + transaction.getAmount());
        } else if (transactionRequest.getCurrency().equals("EUR")) {
            senderAccount.setEurBalance(senderAccount.getEurBalance() - transaction.getAmount());
            receiverAccount.setEurBalance(receiverAccount.getEurBalance() + transaction.getAmount());
        } else {
            // If we get a request with the bad currency code, return 400 response
            return new ResponseEntity<>("Bad currency code", HttpStatus.BAD_REQUEST);
        }


        // Save changes in the database. If there is any error, send the 500 response
        try {
            openBankingService.createTransaction(transaction);
            openBankingService.updateAccount(senderAccount.getId(), senderAccount);
            openBankingService.updateAccount(receiverAccount.getId(), receiverAccount);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to make transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    // Create a new account with no money
    @PostMapping(path = "/payments/initiate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAccount(@RequestBody AccountCreateRequest accountCreateRequest) {
        // TODO: Set first two letters depending on the country from which the account is being registered
        StringBuilder ibanBuilder = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            ibanBuilder.append(Math.random() * 25 + 'A');
        }
        for (int i = 0; i < 19; i++) {
            ibanBuilder.append(Math.random() * 9 + '0');
        }

        Account account = new Account();
        account.setSurname(accountCreateRequest.getSurname());
        account.setFirstname(accountCreateRequest.getFirstname());
        account.setIban(ibanBuilder.toString());
        account.setUsdBalance(0.0f);
        account.setEurBalance(0.0f);

        try {
            openBankingService.createAccount(account);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}