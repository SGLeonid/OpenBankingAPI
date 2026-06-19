package org.forestwizard.springdemo.wallet;

import lombok.RequiredArgsConstructor;
import org.forestwizard.springdemo.authentication.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepositoryI walletRepository;

    public Wallet getWallet(AuthenticatedUser user, String iban) {
        return walletRepository.findByUserAndIban(user, iban).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found")
        );
    }

    public void attachWallet(AuthenticatedUser user, String iban) {
        if (walletRepository.findByIban(iban).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The wallet with such IBAN currently exists");
        }
        Wallet wallet = Wallet
                .builder()
                .user(user)
                .iban(iban)
                .usdAmount(BigDecimal.ZERO)
                .eurAmount(BigDecimal.ZERO)
                .uahAmount(BigDecimal.ZERO)
                .build();

        walletRepository.save(wallet);
    }

    public void updateWalletBalance(String iban, String currency, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIban(iban).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found")
        );

        switch (currency) {
            case "usd" -> wallet.setUsdAmount(amount);
            case "eur" -> wallet.setEurAmount(amount);
            case "uah" -> wallet.setUahAmount(amount);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported currency");
        }

        walletRepository.save(wallet);
    }
}
