package org.forestwizard.springdemo.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticatedUserRepositoryI extends JpaRepository<AuthenticatedUser, Integer> {
    Optional<AuthenticatedUser> findByUsername(String username);
    // boolean selectExistsEmail(String email);
    // boolean selectExistsUsername(String username);
}
