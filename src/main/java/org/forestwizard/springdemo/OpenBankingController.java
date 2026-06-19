package org.forestwizard.springdemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.forestwizard.springdemo.authentication.AuthenticatedUser;;
import org.forestwizard.springdemo.response.BalanceResponse;
import org.forestwizard.springdemo.response.TransactionResponse;
import org.forestwizard.springdemo.response.UserInfoResponse;
import org.forestwizard.springdemo.transaction.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
@Tag(name = "Banking management", description = "Utilities for managing banking")
public class OpenBankingController {
    private final TransactionService transactionService;

    @Operation(summary = "Get currenct account info", description = "Returns main information about current account")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the info",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserInfoResponse.class))
    )
    @GetMapping("/accounts/info")
    public ResponseEntity<UserInfoResponse> getAccountInfo(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return ResponseEntity.ok(
                UserInfoResponse.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build()
        );
    }

    @Operation(summary = "Get currenct wallet balance", description = "Returns the balance and requested currency")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the currency balance info",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BalanceResponse.class)
            )
    )
    @GetMapping("/accounts/{iban}/balance")
    public ResponseEntity<BalanceResponse> getAccountBalance(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable String iban,
            @RequestParam String currency
    ) {
        return ResponseEntity.ok(transactionService.getCurrencyInfo(user, iban, currency));
    }

    @Operation(summary = "Get the last 10 performed transactions", description = "Returns the list of last 10 transactions")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the transaction history",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))
            )
    )
    @GetMapping("/accounts/{iban}/transactions")
    ResponseEntity<List<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable String iban
    ) {
        return ResponseEntity.ok(transactionService.getLast10TransactionsBySender(user));
    }

    @Operation(summary = "Perform a transaction", description = "Returns the payment info gotten from external API")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully performed a payment and retrieved corresponding info",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionResponse.class)
            )
    )
    @PostMapping("/payments/initiate")
    ResponseEntity<TransactionResponse> initiatePayment(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam String senderIban,
            @RequestParam String receiverIban,
            @RequestParam String currency,
            @RequestParam BigDecimal amount
    ) {
        return ResponseEntity.ok(transactionService.initiatePayment(user, senderIban, receiverIban, currency, amount));
    }
}
