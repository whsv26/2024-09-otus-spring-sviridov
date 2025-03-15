package me.whsv26.user.application;

import lombok.RequiredArgsConstructor;
import me.whsv26.user.domain.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User register(String username, String password) {
        var passwordHash = passwordEncoder.encode(password);
        var user = new User(username, passwordHash);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException cause) {
            throw new UserAlreadyExistsException(user.getUsername(), cause);
        }
    }
}
