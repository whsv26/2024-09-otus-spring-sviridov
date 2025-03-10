package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String DB_BACKEND = "database";

    private final UserRepository userRepository;

    @Override
    @Retry(name = DB_BACKEND)
    @CircuitBreaker(name = DB_BACKEND)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("User with username" + username + " is not found")
        );

        return User.withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getAuthorities())
            .build();
    }
}
