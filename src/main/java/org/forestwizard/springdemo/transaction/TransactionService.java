package org.forestwizard.springdemo.transaction;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.forestwizard.springdemo.authentication.AuthenticatedUser;
import org.forestwizard.springdemo.response.BalanceResponse;
import org.forestwizard.springdemo.response.TransactionResponse;
import org.forestwizard.springdemo.wallet.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final WalletService walletService;
    private final TransactionRepositoryI transactionRepository;
    private final WebClient webClient;

    public TransactionService(
            WalletService walletService,
            TransactionRepositoryI repository
    ) {
        this.walletService = walletService;
        this.transactionRepository = repository;
        this.webClient = WebClient.builder().baseUrl("http://localhost:1010/").build();
    }

    public BalanceResponse getCurrencyInfo(
            AuthenticatedUser user,
            String iban,
            String currency
    )  {
        BalanceResponse balanceResponse = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/extpay/accounts/{iban}/balance")
                        .queryParam("currency", currency)
                        .build(iban)
                )
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(
                                new UsernameNotFoundException("Wallet with IBAN" + iban + " not found")
                        )
                )
                .onStatus(
                        status -> status.value() == 400,
                        response -> Mono.error(
                                new BadRequestException("Currency not specified or unsupported")
                        )
                )
                .bodyToMono(BalanceResponse.class)
                .block();
        System.out.println("Sent account balance request to EXTPAY for " + iban);
        System.out.println(balanceResponse);
        walletService.updateWalletBalance(iban, currency, balanceResponse.getAmount());
        return balanceResponse;
    }

    // Get all transactions in the DB
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    // Get one transaction by UUID
    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(id + " not found!")
        );
    }

    // Get all transactions of a sender
    public List<Transaction> getTransactionsBySender(AuthenticatedUser sender) {
        return transactionRepository.findBySender(sender);
    }

    public List<TransactionResponse> getLast10TransactionsBySender(AuthenticatedUser sender) {
        List<TransactionResponse> transactions = new ArrayList<TransactionResponse>();
        List<Transaction> dbTransactions = transactionRepository.findTop10BySenderOrderByIdDesc(sender);
        for (Transaction transaction : dbTransactions) {
            transactions.add(
                    TransactionResponse
                            .builder()
                            .paymentId(transaction.getExternalUuid())
                            .senderId(transaction.getSenderIban())
                            .receiverId(transaction.getReceiverIban())
                            .currency(transaction.getCurrency())
                            .amount(transaction.getAmount())
                            .status(transaction.getStatus())
                            .build()
            );
        }
        return transactions;
    }

    @Transactional(rollbackOn = Exception.class)
    public TransactionResponse initiatePayment(
        AuthenticatedUser user,
        String senderIban,
        String receiverIban,
        String currency,
        BigDecimal amount
    ) {
        // STEP 1 - Get actual currency balance info and validate before transaction
        BalanceResponse balanceResponse = getCurrencyInfo(user, senderIban, currency);
        BigDecimal userAmount = balanceResponse.getAmount();
        if (!(userAmount.compareTo(BigDecimal.ZERO) > 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad amount parameter");
        }
        if (userAmount.compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Not enough money to perform payment");
        }

        // STEP 2 - Pre-save the transaction info in local DB before sending to EXTPAY
        Transaction transaction = Transaction
                .builder()
                .sender(user)
                .senderIban(senderIban)
                .receiverIban(receiverIban)
                .currency(currency)
                .amount(amount)
                .status("IN_PROGRESS")
                .build();
        transaction = transactionRepository.save(transaction);

        // STEP 3 - Send request to EXTPAY
        System.out.println(senderIban + " " + receiverIban + " " + currency + " " + amount);
        TransactionResponse transactionResponse = webClient
                .post()
                .uri(
                uriBuilder -> uriBuilder.path("/extpay/payments/initiate")
                        .queryParam("senderId", senderIban)
                        .queryParam("receiverId", receiverIban)
                        .queryParam("currency", currency)
                        .queryParam("amount", amount)
                        .build()
                )
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(
                                new UsernameNotFoundException("Wallet with such IBAN not found")
                        )
                )
                .onStatus(
                        status -> status.value() == 400,
                        response -> Mono.error(
                                new BadRequestException("Bad request parameters")
                        )
                )
                .bodyToMono(TransactionResponse.class)
                .block();

        // STEP 4 - Update currency balance info in sender's wallet
        // Or request the actual balance from EXTPAY to know the correct one

        if (!transactionResponse.getStatus().equals("INITIATED")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "External API error");
        }
        transaction.setExternalUuid(transactionResponse.getPaymentId());
        transaction.setStatus("INITIATED");
        transactionRepository.save(transaction);

        // STEP 5 - Return response
        System.out.println(transactionResponse);
        return transactionResponse;
    }
}
