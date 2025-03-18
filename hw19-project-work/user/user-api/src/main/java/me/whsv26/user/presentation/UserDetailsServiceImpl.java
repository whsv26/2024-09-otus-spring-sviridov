package me.whsv26.user.presentation;

import lombok.AllArgsConstructor;
import me.whsv26.user.application.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("User with username" + username + " is not found")
        );

        var userDetails = User.withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getAuthorities())
            .build();

        return new RichUserDetails(userDetails, user.getId());
    }
}
