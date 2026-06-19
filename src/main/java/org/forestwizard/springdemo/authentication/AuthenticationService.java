package org.forestwizard.springdemo.authentication;

import lombok.RequiredArgsConstructor;
import org.forestwizard.springdemo.Role;
import org.forestwizard.springdemo.jwt.JwtService;
import org.forestwizard.springdemo.request.AuthenticationRequest;
import org.forestwizard.springdemo.request.RegisterRequest;
import org.forestwizard.springdemo.response.AuthenticationResponse;
import org.forestwizard.springdemo.wallet.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticatedUserRepositoryI authenticatedUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final WalletService walletService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (authenticatedUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user with such username already exists");
        }
        if (request.getUsername().isEmpty() || request.getPassword().isEmpty() || request.getIban().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request parameters not specified");
        }

        AuthenticatedUser user = AuthenticatedUser
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        authenticatedUserRepository.save(user);
        walletService.attachWallet(user, request.getIban());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println(request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        AuthenticatedUser user = authenticatedUserRepository
                .findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException(request.getUsername() + " not found!")
                );
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}

